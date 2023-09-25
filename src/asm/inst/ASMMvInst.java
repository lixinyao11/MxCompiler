package asm.inst;

import asm.operand.Register;
import asm.operand.VirtualRegister;
import asm.section.ASMBlock;

import java.util.HashSet;

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
  public Register def() { return rd; }
  public void setDef(Register reg) { rd = reg; }
  public Register use1() { return rs; }
  public void setUse1(Register reg) { rs = reg; }
}
