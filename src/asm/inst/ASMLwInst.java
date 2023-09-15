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
  @Override
  public void initUseDef(HashSet<VirtualRegister> use, HashSet<VirtualRegister> def) {
    if (rd instanceof VirtualRegister)
      def.add((VirtualRegister) rd);
  }
}
