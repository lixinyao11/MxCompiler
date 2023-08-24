package ir.inst;

import ir.IRBlock;
import ir.IRVisitor;
import ir.util.entity.*;

public class IRBinaryInst extends IRInst {
  public LocalVar result = null;
  public String op = null;
  public IREntity rhs1 = null, rhs2 = null; // LocalVar(virtualReg) or Literal
  // lhs, rhs1, rhs2 must be the same type
  // 不可能是ptr type

  public IRBinaryInst(IRBlock parent, LocalVar result, String op, IREntity rhs1, IREntity rhs2) {
    super(parent);
    if (!result.getType().equals(rhs1.getType()) || !result.getType().equals(rhs2.getType()) || !rhs1.getType().equals(rhs2.getType()))
      throw new RuntimeException("Binary: type not match");
    this.result = result;
    this.rhs1 = rhs1;
    this.rhs2 = rhs2;
    if (op.equals("&&")) op = "&";
    if (op.equals("||")) op = "|";
    this.op = op;
  }

  public String toString() {
    String tmp = switch (op) {
      case "+" -> "add";
      case "-" -> "sub";
      case "*" -> "mul";
      case "/" -> "sdiv";
      case "%" -> "srem";
      case "<<" -> "shl";
      case ">>" -> "ashr";
      case "&" -> "and";
      case "|" -> "or";
      case "^" -> "xor";
      default -> throw new RuntimeException("Binary: unknown op:" + op);
    };
    return result.toString() + " = " + tmp + " " + result.getType().toString() + " " + rhs1.toString() + ", " + rhs2.toString();
  }

  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
