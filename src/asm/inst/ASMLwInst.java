package asm.inst;

import asm.operand.MemAddr;
import asm.operand.Register;
import asm.operand.VirtualRegister;
import asm.section.ASMBlock;

import java.util.HashSet;

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
  public Register def() { return rd; }
  public Register use1() { return addr.base; }
  public void setDef(Register reg) { rd = reg; }
  public void setUse1(Register reg) { addr.base = reg; }
}
