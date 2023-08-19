package ir.inst;

import ir.util.entity.*;
import ir.util.IRType;

public class Icmp extends IRInst {
  public LocalVar result = null; // must be i1
  public String op = null;
  public IREntity rhs1 = null, rhs2 = null; // rhs1, rhs2 must be the same type

  public Icmp(LocalVar result, String op, IREntity rhs1, IREntity rhs2) {
    if (!result.getType().equals(new IRType("i1")) || !rhs1.getType().equals(rhs2.getType()))
      throw new RuntimeException("Icmp: type not match");
    this.result = result;
    this.rhs1 = rhs1;
    this.rhs2 = rhs2;
    switch (op) {
      case ">" -> this.op = "sgt";
      case ">=" -> this.op = "sge";
      case "<" -> this.op = "slt";
      case "<=" -> this.op = "sle";
      case "==" -> this.op = "eq";
      case "!=" -> this.op = "ne";
      default -> throw new RuntimeException("Icmp: unknown op");
    }
  }

  public String toString() {
    return result.toString() + " = icmp " + op + " " + rhs1.getType().toString() + " " + rhs1.toString() + ", " + rhs2.toString();
  }
}
