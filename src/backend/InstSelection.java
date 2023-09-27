package backend;

import asm.*;
import asm.inst.*;
import asm.operand.*;
import asm.section.*;
import ir.*;
import ir.inst.*;
import ir.module.*;
import ir.util.IRType;
import ir.util.entity.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.min;

public class InstSelection implements IRVisitor {
  ASMProgram program;
  ASMBlock currentBlock = null;
  ASMFunction currentFunc = null;
  HashMap<String, VirtualRegister> localVars = null;
  HashMap<String, ASMBlock> blocks = null;
  ArrayList<ASMRetInst> retInsts = null;
  PhysicalRegister sp, ra;
  PhysicalRegister[] aRegs = new PhysicalRegister[8];
  PhysicalRegister[] tRegs = new PhysicalRegister[7];
  PhysicalRegister[] sRegs = new PhysicalRegister[12];

  public InstSelection(ASMProgram program) {
    this.program = program;
    sp = PhysicalRegister.get("sp");
    ra = PhysicalRegister.get("ra");
    for (int i = 0; i < 8; ++i)
      aRegs[i] = PhysicalRegister.get("a" + i);
    for (int i = 0; i < 7; ++i)
      tRegs[i] = PhysicalRegister.get("t" + i);
    for (int i = 0; i < 12; ++i)
      sRegs[i] = PhysicalRegister.get("s" + i);
  }

  public void visit(IRProgram program) {
    for (var funcDecl : program.funcDecls)
      funcDecl.accept(this);
    for (var structDef : program.structs.values())
      structDef.accept(this);
    for (var global : program.globals.values())
      global.accept(this);
    for (var stringLiteral : program.stringLiterals)
      stringLiteral.accept(this);
    for (var funcDef : program.functions.values())
      funcDef.accept(this);
  }
  public void visit(IRBlock block) {
    currentBlock = currentBlock.parent.addBlock(block.parent.name + "_" + block.label);
    currentBlock.irBlock = block;
    block.asmBlock = currentBlock;
    blocks.put(block.label, currentBlock);
    for (var inst : block.instructions) {
      inst.accept(this);
    }
  }
  public void visit(IRFuncDef funcDef) {
    VirtualRegister.reset();
    this.localVars = new HashMap<>();
    this.retInsts = new ArrayList<>();
    this.blocks = new HashMap<>();
    var startBlock = currentBlock = program.addFunction(funcDef.name);
    currentFunc = startBlock.parent;

    for (int i = 0; i < funcDef.paras.size(); ++i) {
      VirtualRegister tmp = new VirtualRegister();
      if (i < 8)
        currentBlock.addInst(new ASMMvInst(currentBlock, tmp, aRegs[i]));
      else
        currentBlock.addInst(new ASMLwInst(currentBlock, tmp, new MemAddr(new Immediate(4 * (i - 8)), sp)));
      localVars.put(funcDef.paras.get(i).getName(), tmp);
    }

    for (var block : funcDef.body) {
      block.accept(this); // 遇到LocalVar，去找找manager里的para或者virtual register
    }
    for (var block : funcDef.body) {
      List<IRPhiInst> phis = new ArrayList<>(block.phiInsts.values());
      phis.sort(Comparator.comparing(IRPhiInst::toString));
      phis.forEach(phi -> phi.accept(this));
//      block.phiInsts.forEach((key, phi) -> {
//        phi.accept(this);
//      });
    }

    currentBlock = startBlock;
    // 移动sp，提供本函数需要的栈空间，保存s0-s11
    MemAddr[] sAddr = new MemAddr[12];
    for (int i = 0; i < 12; ++i) {
      sAddr[i] = new MemAddr(new Immediate(currentFunc.stackSize), sp);
      currentFunc.stackSize += 4;
    }
    for (int i = 0; i < 12; ++i)
      currentBlock.addFirstInst(new ASMSwInst(currentBlock, sRegs[i], sAddr[i]));
    currentFunc.moveSpInst = (ASMArithImmInst) currentBlock.addFirstInst(new ASMArithImmInst(currentBlock, "+", sp, sp, new Immediate(-0)));

    // 所有ret前恢复sp，恢复reg
    for (var inst : retInsts) {
      currentBlock = inst.parent;
      int index = currentBlock.insts.indexOf(inst);
      var tmp = new ASMArithImmInst(currentBlock, "+", sp, sp, new Immediate(0));
      currentBlock.insts.add(index, tmp);
      currentFunc.restoreSpInsts.add(tmp);
      for (int i = 0; i < 12; ++i)
        currentBlock.insts.add(index, new ASMLwInst(currentBlock, sRegs[i], sAddr[i]));
    }

    currentFunc.virtualCnt = VirtualRegister.cnt;
  }
  public void visit(IRFuncDecl funcDecl) {}
  public void visit(IRGlobalVarDef globalVarDef) {
    program.globalVars.add(new ASMGlobalVar(globalVarDef.var.name, globalVarDef.init.getIntValue()));
  }
  public void visit(IRStringLiteralDef stringLiteralDef) {
    program.strings.add(new ASMStringLiteral(stringLiteralDef.ptr.name, stringLiteralDef.orgValue));
  }
  public void visit(IRStructDef structDef) {}
  public void visit(IRAllocaInst inst) {
    throw new RuntimeException("InstSelection.visit(IRAllocaInst)");
  }
  public void visit(IRBinaryInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    currentBlock.addInst(new ASMArithInst(currentBlock, inst.op, getIREntity(inst.result), getIREntity(inst.rhs1), getIREntity(inst.rhs2)));
  }
  public void visit(IRBrInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    currentBlock.addInst(new ASMBranchInst(currentBlock, "==", getIREntity(inst.cond), null, new Label(inst.parent.parent.name + "_" + inst.elseBlock.label)));
  }
  public void visit(IRCallInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));

    // save ra and t0-t6 and a0-a7
    var raAddr = new MemAddr(new Immediate(currentFunc.stackSize), sp);
    currentFunc.stackSize += 4;
    MemAddr[] tAddr = new MemAddr[7];
    for (int i = 0; i < 7; ++i) {
      tAddr[i] = new MemAddr(new Immediate(currentFunc.stackSize), sp);
      currentFunc.stackSize += 4;
    }
    MemAddr[] aAddr = new MemAddr[8];
    for (int i = 0; i < 8; ++i) {
      aAddr[i] = new MemAddr(new Immediate(currentFunc.stackSize), sp);
      currentFunc.stackSize += 4;
    }
    currentBlock.addInst(new ASMSwInst(currentBlock, ra, raAddr));
    for (int i = 0; i < 7; ++i)
      currentBlock.addInst(new ASMSwInst(currentBlock, tRegs[i], tAddr[i]));
    for (int i = 0; i < 8; ++i)
      currentBlock.addInst(new ASMSwInst(currentBlock, aRegs[i], aAddr[i]));

    // ! 处理参数，如果有溢出参数移动sp
    for (int i = 0; i < min(inst.args.size(), 8); ++i)
      currentBlock.addInst(new ASMMvInst(currentBlock, aRegs[i], getIREntity(inst.args.get(i))));
    int spOffset = 0;
    if (inst.args.size() > 8) {
      for (int i = 0; i < inst.args.size() - 8; ++i)
        currentBlock.addInst(new ASMSwInst(currentBlock, getIREntity(inst.args.get(i + 8)), new MemAddr(new Immediate(-(inst.args.size() - 8 - i) * 4), sp)));
      spOffset = (inst.args.size() - 8) * 4;
      currentBlock.addInst(new ASMArithImmInst(currentBlock, "+", sp, sp, new Immediate(-spOffset)));
    }
    // call
    currentBlock.addInst(new ASMCallInst(currentBlock, new Label(inst.funcName)));
    // ! 恢复sp
    if (spOffset > 0) {
      currentBlock.addInst(new ASMArithImmInst(currentBlock, "+", sp, sp, new Immediate(spOffset)));
    }
    // 从a0中取出返回值
    if (inst.result != null)
      currentBlock.addInst(new ASMMvInst(currentBlock, getIREntity(inst.result), aRegs[0]));

    // restore ra and t0-t6 and a0-a7
    currentBlock.addInst(new ASMLwInst(currentBlock, ra, raAddr));
    for (int i = 0; i < 7; ++i)
      currentBlock.addInst(new ASMLwInst(currentBlock, tRegs[i], tAddr[i]));
    for (int i = 0; i < 8; ++i)
      currentBlock.addInst(new ASMLwInst(currentBlock, aRegs[i], aAddr[i]));
  }
  public void visit(IRGetElementPtrInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    var ptr = getIREntity(inst.ptr);
    var index = getIREntity(inst.indexs.get(inst.indexs.size() - 1));
    var tmp1 = new VirtualRegister();
    var tmp2 = new VirtualRegister();
    currentBlock.addInst(new ASMArithInst(currentBlock, "*", tmp1, index, getIREntity(new IRLiteral("4", new IRType("i32")))));
    currentBlock.addInst(new ASMArithInst(currentBlock, "+", tmp2, ptr, tmp1));

    currentBlock.addInst(new ASMMvInst(currentBlock, getIREntity(inst.result), tmp2));
  }
  public void visit(IRIcmpInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));

    var reg1 = getIREntity(inst.rhs1);
    var reg2 = getIREntity(inst.rhs2);
    var tmp1 = new VirtualRegister();
    var tmp2 = new VirtualRegister();

    // sub reg1, reg2
    currentBlock.addInst(new ASMArithInst(currentBlock, "-", tmp1, reg1, reg2));
    // set if __ 0
    switch (inst.op) {
      case ">", "==", "<", "!=" -> {
        currentBlock.addInst(new ASMSetInst(currentBlock, inst.op, tmp2, tmp1));
        currentBlock.addInst(new ASMMvInst(currentBlock, getIREntity(inst.result), tmp2));
      }
      case ">=" -> {
        var tmp3 = new VirtualRegister();
        var tmp4 = new VirtualRegister();
        currentBlock.addInst(new ASMSetInst(currentBlock, ">", tmp2, tmp1));
        currentBlock.addInst(new ASMSetInst(currentBlock, "==", tmp3, tmp1));
        currentBlock.addInst(new ASMArithInst(currentBlock, "|", tmp4, tmp2, tmp3));
        currentBlock.addInst(new ASMMvInst(currentBlock, getIREntity(inst.result), tmp4));
      }
      case "<=" -> {
        var tmp3 = new VirtualRegister();
        var tmp4 = new VirtualRegister();
        currentBlock.addInst(new ASMSetInst(currentBlock, "<", tmp2, tmp1));
        currentBlock.addInst(new ASMSetInst(currentBlock, "==", tmp3, tmp1));
        currentBlock.addInst(new ASMArithInst(currentBlock, "|", tmp4, tmp2, tmp3));
        currentBlock.addInst(new ASMMvInst(currentBlock, getIREntity(inst.result), tmp4));
      }
      default -> throw new RuntimeException("IcmpInst: invalid op");
    }
  }
  public void visit(IRJumpInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    currentBlock.addInst(new ASMJInst(currentBlock, inst.parent.parent.name + "_" + inst.destBlock.label));
  }
  public void visit(IRLoadInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    currentBlock.addInst(new ASMLwInst(currentBlock, getIREntity(inst.result), new MemAddr(new Immediate(0), getIREntity(inst.ptr))));
  }
  public void visit(IRStoreInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    currentBlock.addInst(new ASMSwInst(currentBlock, getIREntity(inst.src), new MemAddr(new Immediate(0), getIREntity(inst.dest))));
  }
  public void visit(IRRetInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));

    // 保存返回值到a0
    if (inst.value != null)
      currentBlock.addInst(new ASMMvInst(currentBlock, aRegs[0], getIREntity(inst.value)));
    retInsts.add((ASMRetInst) currentBlock.addInst(new ASMRetInst(currentBlock)));
  }
  public void visit(IRPhiInst inst) {
    Register result = getIREntity(inst.result);
    for (var block : inst.blocks) {
      currentBlock = blocks.get(block.label);
      var exitInst = currentBlock.insts.get(currentBlock.insts.size() - 1);
      if (exitInst instanceof ASMBranchInst || exitInst instanceof ASMJInst || exitInst instanceof ASMRetInst)
        currentBlock.insts.remove(exitInst);
      currentBlock.addInst(new ASMMvInst(currentBlock, result, getIREntity(inst.values.get(inst.blocks.indexOf(block)))));
      if (exitInst instanceof ASMBranchInst || exitInst instanceof ASMJInst || exitInst instanceof ASMRetInst)
        currentBlock.addInst(exitInst);
    }
  }

  private VirtualRegister getIREntity(IREntity entity) {
    if (entity instanceof GlobalPtr) {
      var tmp = new VirtualRegister();
      currentBlock.addInst(new ASMLaInst(currentBlock, tmp, ((GlobalPtr) entity).name));
      return tmp;
    } else if (entity instanceof IRLiteral) {
      var tmp = new VirtualRegister();
      currentBlock.addInst(new ASMLiInst(currentBlock, tmp, new Immediate(((IRLiteral) entity).getIntValue())));
      return tmp;
    } else if (entity instanceof LocalVar) {
      String name = ((LocalVar) entity).name;
      if (localVars.containsKey(name))
        return localVars.get(name);
      else {
        VirtualRegister ret = new VirtualRegister();
        localVars.put(name, ret);
        return ret;
      }
    } else {
      throw new RuntimeException("InstSelection.getIREntity");
    }
  }
}
