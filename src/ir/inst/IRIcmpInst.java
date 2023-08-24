package ir.inst;

import ir.IRBlock;
import ir.IRVisitor;
import ir.util.entity.*;
import ir.util.IRType;

public class IRIcmpInst extends IRInst {
  public LocalVar result = null; // must be i1
  public String op = null;
  public IREntity rhs1 = null, rhs2 = null;
  // rhs1, rhs2 must be the same type
  // can be globalptr or literal or locaLVar

  public IRIcmpInst(IRBlock parent, LocalVar result, String op, IREntity rhs1, IREntity rhs2) {
    super(parent);
    if (!result.getType().equals(new IRType("i1")) || !rhs1.getType().equals(rhs2.getType()))
      throw new RuntimeException("Icmp: type not match");
    this.result = result;
    this.rhs1 = rhs1;
    this.rhs2 = rhs2;
    this.op = op;
  }

  public String toString() {
    String tmp = switch (op) {
      case ">" -> "sgt";
      case ">=" -> "sge";
      case "<" -> "slt";
      case "<=" -> "sle";
      case "==" -> "eq";
      case "!=" -> "ne";
      default -> throw new RuntimeException("Icmp: unknown op");
    };
    return result.toString() + " = icmp " + tmp + " " + rhs1.getType().toString() + " " + rhs1.toString() + ", " + rhs2.toString();
  }

  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
