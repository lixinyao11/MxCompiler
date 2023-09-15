package asm.section;

import asm.inst.ASMInst;
import asm.operand.VirtualRegister;
import ir.IRBlock;
import org.antlr.v4.runtime.atn.ContextSensitivityInfo;

import java.util.ArrayList;
import java.util.HashSet;

public class ASMBlock extends ASMSection {
  public ArrayList<ASMInst> insts = null;
  public HashSet<VirtualRegister> use = null;
  public HashSet<VirtualRegister> def = null;
  public HashSet<VirtualRegister> liveIn = null;
  public HashSet<VirtualRegister> liveOut = null;
  public IRBlock irBlock = null; // null represents startBlock
  public ArrayList<ASMBlock> preds = null;
  public ArrayList<ASMBlock> succs = null;

  public ASMBlock(String label) {
    super(label);
    this.insts = new ArrayList<>();
    this.liveIn = new HashSet<>();
    this.liveOut = new HashSet<>();
    this.preds = new ArrayList<>();
    this.succs = new ArrayList<>();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("      .section text\n").append("      .globl ").append(label).append("\n");
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
      inst.initUseDef(use, def);
    }
  }

}
