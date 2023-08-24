package asm.inst;

import asm.operand.Register;
import asm.section.ASMBlock;

public class ASMArithInst extends ASMInst {
  Register rd = null, rs1 = null, rs2 = null;
  String op = null;

  public ASMArithInst(ASMBlock parent, String op, Register rd, Register rs1, Register rs2) {
    super(parent);
    this.op = op;
    this.rd = rd;
    this.rs1 = rs1;
    this.rs2 = rs2;
  }

  @Override
  public String toString() {
    String tmp = switch (op) {
      case "+" -> "add";
      case "-" -> "sub";
      case "*" -> "mul";
      case "/" -> "div";
      case "%" -> "rem";
      case "<<" -> "sll";
      case ">>" -> "sra";
      case "^" -> "xor";
      case "|" -> "or";
      case "&" -> "and";
      default -> throw new RuntimeException("ASMArithInst: unknown op: " + op);
    };
    return String.format("%-8s", tmp) + rd + ", " + rs1 + ", " + rs2;
  }
}
