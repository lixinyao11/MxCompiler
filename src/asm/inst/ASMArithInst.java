package asm.inst;

import asm.operand.Register;
import asm.operand.VirtualRegister;
import asm.section.ASMBlock;

import java.util.HashSet;

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
  public Register def() { return rd; }
  public void setDef(Register reg) { rd = reg; }
  public Register use1() { return rs1; }
  public void setUse1(Register reg) { rs1 = reg; }
  public Register use2() { return rs2; }
  public void setUse2(Register reg) { rs2 = reg; }
}
