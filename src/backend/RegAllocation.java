package backend;

import asm.ASMProgram;
import asm.inst.*;
import asm.operand.*;
import asm.section.*;
import ir.IRBlock;
import ir.inst.IRBinaryInst;
import util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class RegAllocation {
  ASMProgram program = null;
  ASMFunction curFunc = null;
  public static final int K = 27;
  HashSet<Register> precolored = new HashSet<>(); // PhysicalRegister,precolored
  HashSet<Register> initial = new HashSet<>(); // VirtualRegister, not yet processed
  ArrayList<Register> simplifyWorklist = new ArrayList<>(); // low degree non-move-related
  ArrayList<Register> freezeWorklist = new ArrayList<>(); // low degree move-related
  ArrayList<Register> spillWorklist = new ArrayList<>(); // high degree
  HashSet<Register> spilledNodes = new HashSet<>(); // nodes marked for spilling during this round
  HashSet<Register> coalescedNodes = new HashSet<>(); // nodes that have been coalesced
  HashSet<Register> coloredNodes = new HashSet<>(); // nodes successfully colored
  Stack<Register> selectStack = new Stack<>(); // stack of temporaries removed from graph
  ArrayList<ASMMvInst> coalescedMoves = new ArrayList<>(); // moves that have been coalesced
  ArrayList<ASMMvInst> constrainedMoves = new ArrayList<>(); // moves whose source and target interfere
  ArrayList<ASMMvInst> frozenMoves = new ArrayList<>(); // moves that are not yet ready for coalescing
  ArrayList<ASMMvInst> worklistMoves = new ArrayList<>(); // moves enabled for possible coalescing
  ArrayList<ASMMvInst> activeMoves = new ArrayList<>(); // moves not yet ready for coalescing
  HashSet<Pair<Register, Register>> adjSet = new HashSet<>(); // edges in the graph
  HashMap<Register, HashSet<Register>> adjList = new HashMap<>(); // adjacent nodes
  HashMap<Register, Integer> degree = new HashMap<>();
  HashMap<Register, HashSet<ASMMvInst>> moveList = new HashMap<>(); // moves that associate this node
  HashMap<Register, Register> alias = new HashMap<>(); // temporary -> temporary
  HashMap<Register, Integer> color = new HashMap<>(); // temporary -> color
  HashSet<Register> spillTemp = new HashSet<>();

  public RegAllocation(ASMProgram program) {
    this.program = program;
  }
  public void work() {
    program.functions.forEach(this::workOnFunc);
  }
  private void workOnFunc(ASMFunction func) {
    curFunc = func;
    spillTemp.clear();
    while (true) {
      new LiveAnalysis().workOnFunc(func);
      initAll();
      Build();
      MakeWorklist();
      do {
        if (!simplifyWorklist.isEmpty()) Simplify();
        else if (!worklistMoves.isEmpty()) Coalesce();
        else if (!freezeWorklist.isEmpty()) Freeze();
        else if (!spillWorklist.isEmpty()) SelectSpill();
      } while (!simplifyWorklist.isEmpty() || !worklistMoves.isEmpty() || !freezeWorklist.isEmpty() || !spillWorklist.isEmpty());
      AssignColors();
      if (spilledNodes.isEmpty()) break;
      RewriteProgram();
    }

    for (var block : curFunc.blocks) {
      ArrayList<ASMInst> newInsts = new ArrayList<>();
      for (var inst : block.insts) {
        if (inst.def() != null && inst.def() instanceof VirtualRegister)
          inst.setDef(getColor(inst.def()));
        if (inst.use1() != null && inst.use1() instanceof VirtualRegister)
          inst.setUse1(getColor(inst.use1()));
        if (inst.use2() != null && inst.use2() instanceof VirtualRegister)
          inst.setUse2(getColor(inst.use2()));
        if (!(inst instanceof ASMMvInst) || inst.use1() != inst.def())
          newInsts.add(inst);
      }
      block.insts = newInsts;
    }
  }
  private PhysicalRegister getColor(Register reg) {
    int tmp = color.get(reg);
    return PhysicalRegister.get(tmp);
  }
  private void RewriteProgram() {
    VirtualRegister.cnt = curFunc.virtualCnt;
    HashMap<Register, MemAddr> spillAddr = new HashMap<>();
    for (var reg : spilledNodes) {
      MemAddr addr = new MemAddr(new Immediate(curFunc.stackSize), PhysicalRegister.get("sp"));
      curFunc.stackSize += 4;
      spillAddr.put(reg, addr);
    }

    for (var block : curFunc.blocks) {
      for (int i = 0; i < block.insts.size(); ++i) {
        var inst = block.insts.get(i);
        if (inst.def() != null && spilledNodes.contains(inst.def())) {
          MemAddr addr = spillAddr.get(inst.def());
          var tmp = new VirtualRegister();
          inst.setDef(tmp);
          spillTemp.add(tmp);
          block.insts.add(i + 1, new ASMSwInst(block, tmp, addr));
        }
        if (inst.use1() != null && spilledNodes.contains(inst.use1())) {
          MemAddr addr = spillAddr.get(inst.use1());
          var tmp = new VirtualRegister();
          inst.setUse1(tmp);
          spillTemp.add(tmp);
          block.insts.add(i, new ASMLwInst(block, tmp, addr));
        }
        if (inst.use2() != null && spilledNodes.contains(inst.use2())) {
          MemAddr addr = spillAddr.get(inst.use2());
          var tmp = new VirtualRegister();
          inst.setUse2(tmp);
          spillTemp.add(tmp);
          block.insts.add(i, new ASMLwInst(block, tmp, addr));
        }
      }
    }

//    curFunc.moveSpInst.imm = new Immediate(-curFunc.stackSize);
//    for (var inst : curFunc.restoreSpInsts) {
//      inst.imm = new Immediate(curFunc.stackSize);
//    }
  }
  private void AssignColors() {
    while (!selectStack.isEmpty()) {
      Register reg = selectStack.pop();
      HashSet<Integer> okColors = new HashSet<>();
      for (int i = 0; i < K; ++i) okColors.add(i + 5);
      for (var adj : adjList.get(reg)) {
        Register alias = GetAlias(adj);
        if (coloredNodes.contains(alias) || precolored.contains(alias))
          okColors.remove(color.get(alias));
      }
      if (okColors.isEmpty()) spilledNodes.add(reg);
      else {
        coloredNodes.add(reg);
        color.put(reg, okColors.iterator().next());
      }
    }
    for (var reg : coalescedNodes)
      color.put(reg, color.get(GetAlias(reg)));
  }
  private void SelectSpill() {
    Register m = null;
    for (Register reg : spillWorklist)
      if (m == null || reg.spillWeight / degree.get(reg) < m.spillWeight / degree.get(m) && !spillTemp.contains(reg))
        m = reg;
    spillWorklist.remove(m);
    simplifyWorklist.add(m);
    FreezeMoves(m);
  }
  private void Freeze() {
    Register u = freezeWorklist.remove(freezeWorklist.size() - 1);
    simplifyWorklist.add(u);
    FreezeMoves(u);
  }
  private void FreezeMoves(Register reg) {
    for (var mv : NodeMoves(reg)) { // reg是move-related，把它冻结——冻结所有它的mv
      Register x = mv.def(), y = mv.use1();
      Register v;
      if (GetAlias(y) == GetAlias(reg)) v = GetAlias(x);
      else v = GetAlias(y);
      activeMoves.remove(mv); // ! 一定是worklistMoves里没东西了，无法继续合并了，才会到freeze这步，因此mv一定在activeMoves里
      frozenMoves.add(mv);
      // 冻结这条指令可能导致另一个变量v也能解冻，准备simplify了
      if (degree.get(v) < K && NodeMoves(v).isEmpty()) {
        freezeWorklist.remove(v);
        simplifyWorklist.add(v);
      }
    }
  }
  /*
   * 对一句mv尝试合并
   * 注意：要考虑两端节点已经被合并到其它节点的情况
   *
   * 两端相同：直接合并
   * 均预染色or有冲突：不能合并
   * 根据briggs或george原则可以合并：进行合并
   * 其它情况：暂时不能合并（邻接点度数不满足条件）：放入activeMoves等待（如果相关节点度数发生变化，会enableMove
   *
   * 确认合并状态后：检查是否能够从冻结态(即低度数且传送相关,注意现在解除了一个传送)解冻，放入simplifyWorklist
   */
  private void Coalesce() {
    ASMMvInst mv = worklistMoves.remove(worklistMoves.size() - 1);
    Register x = GetAlias(mv.def()), y = GetAlias(mv.use1());
    Register u, v; // ! u is at most precolored
    if (precolored.contains(y)) {
      u = y;
      v = x;
    } else {
      u = x;
      v = y;
    }
    if (u == v) { // 直接合并
      coalescedMoves.add(mv);
      AddWorkList(u);
    } else if (precolored.contains(v) || adjSet.contains(new Pair<>(u, v))) {
      // uv冲突或uv均预染色-->永远不能合并
      constrainedMoves.add(mv);
      AddWorkList(u);
      AddWorkList(v);
    } else if ((precolored.contains(u) && OK(v, u)) || (!precolored.contains(u) && Conservative(adjacent(u), adjacent(v)))) {
      // 符合合并条件-->合并
      coalescedMoves.add(mv);
      Combine(u, v);
      AddWorkList(u);
    } else {
      // 暂时不符合条件无法合并，等待enableMove
      activeMoves.add(mv);
    }
  }
  private void Combine(Register u, Register v) {
    if (freezeWorklist.contains(v)) freezeWorklist.remove(v);
    else spillWorklist.remove(v);
    coalescedNodes.add(v);
    alias.put(v, u);

    moveList.get(u).addAll(moveList.get(v));
    for (var t : adjacent(v)) {
      addEdge(t, u);
      DecrementDegree(t);
    }
    // * 上一操作合并所有邻接点，可能使得u变为高度数节点，需要从freeze转入spill
    if (degree.get(u) >= K && freezeWorklist.contains(u)) {
      freezeWorklist.remove(u);
      spillWorklist.add(u);
    }
  }
  private void AddWorkList(Register reg) {
    // * 如果reg原本是在冻结态，且没有其他的尚未处理的有关move-->解冻
    if (!precolored.contains(reg) && !isMoveRelated(reg) && degree.get(reg) < K) {
      freezeWorklist.remove(reg);
      simplifyWorklist.add(reg);
    }
  }
  private boolean OK(Register v, Register u) {
    // * u is precolored
    // * any t in Adjacent(v), OK(t, u)
    for (var t : adjacent(v)) {
      if (degree.get(t) >= K && !precolored.contains(t) && !adjSet.contains(new Pair<>(t, u))) return false;
    }
    return true;
  }
  private boolean Conservative(HashSet<Register> nodes1, HashSet<Register> nodes2) {
    int k = 0;
    for (var reg : nodes1)
      if (degree.get(reg) >= K) ++k;
    for (var reg : nodes2)
      if (degree.get(reg) >= K) ++k;
    return k < K;
  }
  private Register GetAlias(Register reg) {
    if (coalescedNodes.contains(reg))
      return GetAlias(alias.get(reg));
    else return reg;
  }
  /*
   * 从simplifyWorkList中依次取出节点，压入selectStack
   * 对于每个节点，遍历其邻接表，对于每个邻接点，degree--
   * ATTENTION: 邻接点不包括已经被合并的点和已经被简化(从图中删去)的点
   */
  private void Simplify() {
    Register reg = simplifyWorklist.remove(simplifyWorklist.size() - 1);
    selectStack.push(reg);
    for (var adj : adjacent(reg)) {
      DecrementDegree(adj);
    }
  }
  /*
   * 减少degree
   * 如果本来就是低度数节点，不做变化
   * 否则，需要把它从spillWorklist中拿出来，放入freeze/simplifyWorklist
   *          enable它和它的邻接点的move
   */
  private void DecrementDegree(Register reg) {
    int d = degree.get(reg);
    degree.put(reg, d - 1);
    if (d == K) {
      HashSet<Register> nodes = new HashSet<>(adjacent(reg));
      nodes.add(reg);
      EnableMoves(nodes);
      spillWorklist.remove(reg);
      if (isMoveRelated(reg)) freezeWorklist.add(reg);
      else simplifyWorklist.add(reg);
    }
  }
  private void EnableMoves(HashSet<Register> nodes) {
    for (var reg : nodes) {
      for (var mv : NodeMoves(reg)) {
        if (activeMoves.contains(mv)) {
          activeMoves.remove(mv);
          worklistMoves.add(mv);
        }
      }
    }
  }
  private HashSet<Register> adjacent(Register reg) {
    HashSet<Register> ret = new HashSet<>(adjList.get(reg));
    ret.removeIf(r -> coalescedNodes.contains(r) || selectStack.contains(r));
    return ret;
  }
  /*
   * 遍历initial：degree < K && non-move-related --> simplifyWorklist
   *             degree < K && move-related --> freezeWorklist
   *             degree >= K --> spillWorklist
   *             并从initial中删去
   */
  private void MakeWorklist() {
    for (var reg : initial) {
      if (degree.get(reg) >= K) spillWorklist.add(reg);
      else if (isMoveRelated(reg)) freezeWorklist.add(reg);
      else simplifyWorklist.add(reg);
    }
    initial.clear();
  }
  /*
   * 与reg有关且当前可以合并（未被freeze或合并或限制）的move
   */
  private HashSet<ASMMvInst> NodeMoves(Register reg) {
    HashSet<ASMMvInst> ret = moveList.get(reg);
    ret.removeIf(mv -> !activeMoves.contains(mv) && !worklistMoves.contains(mv));
    return ret;
  }
  private boolean isMoveRelated(Register reg) {
    return !NodeMoves(reg).isEmpty();
  }
  /*
   * 遍历每个block，每条指令，取出每个register
   * 1. 把物理寄存器放入precolored，初始化color，度数为无限大
   * 2. 虚拟寄存器放入initial，每个开一个adjList，度数为0
   * 3. 每个寄存器的degree为0，开一个moveList
   */
  private void initAll() {
    precolored.clear();
    initial.clear();
    simplifyWorklist.clear();
    freezeWorklist.clear();
    spillWorklist.clear();
    spilledNodes.clear();
    coalescedNodes.clear();
    coloredNodes.clear();
    selectStack.clear();

    coalescedMoves.clear();
    constrainedMoves.clear();
    frozenMoves.clear();
    worklistMoves.clear();
    activeMoves.clear();

    adjSet.clear();
    adjList.clear();
    degree.clear();
    moveList.clear();
    alias.clear();
    color.clear();

    // ! 函数参数相关的mv必须预染色！！不能随便染，a0,a1...是固定的
    for (var inst : curFunc.blocks.get(0).insts) {
      if (inst instanceof ASMMvInst) {
        precolored.add(inst.def());
        color.put(inst.def(), ((PhysicalRegister) inst.use1()).getColor() + 8);
        degree.put(inst.def(), Integer.MAX_VALUE);
      }
    }
    for (var block : curFunc.blocks) {
      for (var inst : block.insts) {
        for (var reg : inst.getRegs()) {
          if (isSpRa(reg)) continue;
          moveList.put(reg, new HashSet<>());
          if (reg instanceof PhysicalRegister) {
            precolored.add(reg);
            color.put(reg, ((PhysicalRegister) reg).getColor());
            degree.put(reg, Integer.MAX_VALUE);
          } else if (!precolored.contains(reg)) {
            initial.add(reg);
            adjList.put(reg, new HashSet<>());
            degree.put(reg, 0);
            reg.spillWeight = 0;
          }
        }
      }
    }

    // spill weight
    for (var block : curFunc.blocks) {
      double weight = Math.pow(10, block.getLoopDepth());
      for (var inst : block.insts)
        for (var reg : inst.getRegs())
          reg.spillWeight += weight;
    }
  }
  /*
   * 处理mv：更新moveList和workListMoves
   * 处理所有冲突连边：更新adjSet
   *               对没有pre-color的：更新adjList和degree
   */
  private void Build() {
    for (ASMBlock block : curFunc.blocks) {
      HashSet<Register> live = new HashSet<>(block.liveOut);
      for (int i = block.insts.size() - 1; i >= 0; --i) {
        ASMInst inst = block.insts.get(i);
        if (inst instanceof ASMMvInst) {
          assert inst.def() != null;
          assert inst.use1() != null;
          assert inst.use2() == null;
//          live.remove(inst.use1());
          moveList.get(inst.use1()).add((ASMMvInst) inst);
          moveList.get(inst.def()).add((ASMMvInst) inst);
          worklistMoves.add((ASMMvInst) inst);
        }
        if (inst.def() != null && !isSpRa(inst.def())) {
          for (var l : live)
            if (!isSpRa(l)) addEdge(l, inst.def());
          live.remove(inst.def());
        }
        if (inst.use1() != null && !isSpRa(inst.use1()))
          live.add(inst.use1());
        if (inst.use2() != null && !isSpRa(inst.use2()))
          live.add(inst.use2());
      }
    }
  }
  private void addEdge(Register u, Register v) {
    if (u == v) return;
    if (adjSet.contains(new Pair<>(u, v))) return;
    adjSet.add(new Pair<>(u, v));
    adjSet.add(new Pair<>(v, u));

    if (!precolored.contains(u)) {
      if (u instanceof PhysicalRegister) throw new RuntimeException(((PhysicalRegister) u).name);
      adjList.get(u).add(v);
      degree.put(u, degree.get(u) + 1);
    }
    if (!precolored.contains(v)) {
      if (v instanceof PhysicalRegister) throw new RuntimeException("hehehe");
      adjList.get(v).add(u);
      degree.put(v, degree.get(v) + 1);
    }
  }
  private boolean isSpRa(Register reg) {
    if (reg instanceof VirtualRegister) return false;
    return ((PhysicalRegister) reg).name.equals("sp") || ((PhysicalRegister) reg).name.equals("ra");
  }
}
