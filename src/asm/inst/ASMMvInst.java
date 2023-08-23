package asm.inst;

import asm.operand.Register;
import asm.section.ASMBlock;

public class ASMMvInst extends ASMInst {
  Register rd = null, rs = null;

  public ASMMvInst(ASMBlock parent, Register rd, Register rs) {
    super(parent);
    this.rd = rd;
    this.rs = rs;
  }

  @Override
  public String toString() {
    return String.format("%-8s", "mv") + rd + ", " + rs;
  }
}
