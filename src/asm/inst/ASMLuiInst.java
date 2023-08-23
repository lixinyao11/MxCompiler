package asm.inst;

import asm.operand.*;
import asm.section.ASMBlock;

public class ASMLuiInst extends ASMInst {
  Register rd = null;
  Immediate imm = null;

  public ASMLuiInst(ASMBlock parent, Register rd, Immediate imm) {
    super(parent);
    this.rd = rd;
    this.imm = imm;
  }

  @Override
  public String toString() {
    return String.format("%-8s", "lui") + rd + ", " + imm;
  }
}
