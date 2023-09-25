package asm.inst;

import asm.operand.Register;
import asm.operand.VirtualRegister;
import asm.section.ASMBlock;

import java.util.HashSet;

public class ASMSetInst extends ASMInst {
  Register rd = null, rs = null;
  String op = null;

  public ASMSetInst(ASMBlock parent, String op, Register rd, Register rs) {
    super(parent);
    this.op = op;
    this.rd = rd;
    this.rs = rs;
  }

  @Override
  public String toString() {
    String tmp = switch (op) {
      case "==" -> "seqz";
      case "!=" -> "snez";
      case "<" -> "sltz";
      case ">" -> "sgtz";
      default -> throw new IllegalStateException("Unexpected value: " + op);
    };
    return String.format("%-8s", tmp) + rd + ", " + rs;
  }
  public Register def() { return rd; }
  public void setDef(Register reg) { rd = reg; }
  public Register use1() { return rs; }
  public void setUse1(Register reg) { rs = reg; }
}
