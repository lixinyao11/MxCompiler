package asm.section;

import asm.inst.ASMInst;

import java.util.ArrayList;

public class ASMBlock extends ASMSection {
  public ArrayList<ASMInst> insts = null;

  public ASMBlock(String label) {
    super(label);
    this.insts = new ArrayList<>();
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

}
