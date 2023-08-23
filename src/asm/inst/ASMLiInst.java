package asm.inst;

import asm.operand.*;
import asm.section.ASMBlock;

public class ASMLiInst extends ASMInst {
  Register rd = null;
  Immediate imm = null;

  public ASMLiInst(ASMBlock parent, Register rd, Immediate imm) {
    super(parent);
    this.rd = rd;
    this.imm = imm;
  }
  @Override
  public String toString() {

    return String.format("%-8s", "li") + rd + ", " + imm;
  }
}
