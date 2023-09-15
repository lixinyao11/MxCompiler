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
  @Override
  public void initUseDef(HashSet<VirtualRegister> use, HashSet<VirtualRegister> def) {
    if (rs instanceof VirtualRegister && !def.contains(rs))
      use.add((VirtualRegister) rs);
    if (rd instanceof VirtualRegister)
      def.add((VirtualRegister) rd);
  }
}
