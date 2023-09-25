package asm.section;

import asm.inst.ASMInst;
import asm.operand.Register;
import ir.IRBlock;
import org.antlr.v4.runtime.atn.ContextSensitivityInfo;

import java.util.ArrayList;
import java.util.HashSet;

public class ASMBlock extends ASMSection {
  public ASMFunction parent = null;
  public ArrayList<ASMInst> insts = null;
  public HashSet<Register> use = null;
  public HashSet<Register> def = null;
  public HashSet<Register> liveIn = null;
  public HashSet<Register> liveOut = null;
  public IRBlock irBlock = null; // null represents startBlock
  public ArrayList<ASMBlock> preds = null;
  public ArrayList<ASMBlock> succs = null;

  public ASMBlock(String label, ASMFunction parent) {
    super(label);
    this.parent = parent;
    this.insts = new ArrayList<>();
    this.liveIn = new HashSet<>();
    this.liveOut = new HashSet<>();
    this.preds = new ArrayList<>();
    this.succs = new ArrayList<>();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(label).append(":\n");
    for (ASMInst inst : insts) {
      sb.append("      ").append(inst.toString()).append("\n");
    }
    return sb.toString();
  }

  public ASMInst addInst(ASMInst inst) {
    insts.add(inst);
    return inst;
  }
  public void initUseDef() {
    use = new HashSet<>();
    def = new HashSet<>();
    for (ASMInst inst : insts) {
      if (inst.use1() != null && !def.contains(inst.use1()))
        use.add(inst.use1());
      if (inst.use2() != null && !def.contains(inst.use2()))
        use.add(inst.use2());
      if (inst.def() != null)
        def.add(inst.def());
    }
  }

  public int getLoopDepth() {
    if (irBlock != null) return irBlock.loopDepth;
    else return 0;
  }
}
