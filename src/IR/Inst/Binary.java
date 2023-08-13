package IR.Inst;

import IR.Util.Entity.*;

public class Binary extends IRInst {
  public LocalVar result = null;
  public String op = null;
  public IREntity rhs1 = null, rhs2 = null; // lhs, rhs1, rhs2 must be the same type

  public Binary(LocalVar result, String op, IREntity rhs1, IREntity rhs2) {
    if (!result.getType().equals(rhs1.getType()) || !result.getType().equals(rhs2.getType()) || !rhs1.getType().equals(rhs2.getType()))
      throw new RuntimeException("Binary: type not match");
    this.result = result;
    this.rhs1 = rhs1;
    this.rhs2 = rhs2;
    switch (op) {
      case "+" -> this.op = "add";
      case "-" -> this.op = "sub";
      case "*" -> this.op = "mul";
      case "/" -> this.op = "sdiv";
      case "%" -> this.op = "srem";
      case "<<" -> this.op = "shl";
      case ">>" -> this.op = "ashr";
      case "&" -> this.op = "and";
      case "|" -> this.op = "or";
      case "^" -> this.op = "xor";
      case "&&" -> this.op = "and";
      case "||" -> this.op = "or";
      default -> throw new RuntimeException("Binary: unknown op");
    }
  }

  public String toString() {
    return result.toString() + " = " + op + " " + result.getType().toString() + " " + rhs1.toString() + ", " + rhs2.toString();
  }
}
