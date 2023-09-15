package asm.inst;

import asm.operand.Register;
import asm.operand.VirtualRegister;
import asm.section.ASMBlock;

import java.util.HashSet;

public class ASMLaInst extends ASMInst {
  Register rd = null;
  String symbol = null;

  public ASMLaInst(ASMBlock parent, Register rd, String symbol) {
    super(parent);
    this.rd = rd;
    this.symbol = symbol;
  }

  @Override
  public String toString() {
    return String.format("%-8s", "la") + rd + ", " + symbol;
  }
  @Override
  public void initUseDef(HashSet<VirtualRegister> use, HashSet<VirtualRegister> def) {
    if (rd instanceof VirtualRegister)
      def.add((VirtualRegister) rd);
  }
}
