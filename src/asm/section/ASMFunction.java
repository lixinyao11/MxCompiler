package asm.section;

import asm.inst.ASMArithImmInst;

import java.util.ArrayList;

public class ASMFunction {
  public ArrayList<ASMBlock> blocks = null;
  public String label = null;
  public int virtualCnt = 0;
  public int stackSize = 0;
  public ASMArithImmInst moveSpInst = null;
  public ArrayList<ASMArithImmInst> restoreSpInsts = new ArrayList<>();

  public ASMFunction(String label) {
    this.blocks = new ArrayList<>();
    this.label = label;
  }
  public ASMBlock addBlock(String label) {
    ASMBlock block = new ASMBlock(label, this);
    blocks.add(block);
    return block;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("      .section text\n").append("      .globl ").append(label).append("\n");
    for (ASMBlock block : blocks) {
      sb.append(block);
    }
    return sb.toString();
  }
}
