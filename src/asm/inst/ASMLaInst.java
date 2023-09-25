package asm.inst;

import asm.operand.Register;
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
  public Register def() { return rd; }
  public void setDef(Register reg) { rd = reg; }
}
