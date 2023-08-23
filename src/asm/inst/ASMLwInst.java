package asm.inst;

import asm.operand.MemAddr;
import asm.operand.Register;
import asm.section.ASMBlock;

public class ASMLwInst extends ASMInst {
  Register rd = null;
  MemAddr addr = null;

  public ASMLwInst(ASMBlock parent, Register rd, MemAddr addr) {
    super(parent);
    this.rd = rd;
    this.addr = addr;
  }

  public void setAddr(MemAddr addr) {
    this.addr = addr;
  }

  @Override
  public String toString() {
    return String.format("%-8s", "lw") + rd + ", " + addr;
  }
}
