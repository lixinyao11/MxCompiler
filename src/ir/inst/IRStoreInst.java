package ir.inst;

import ir.IRBlock;
import ir.IRVisitor;
import ir.util.entity.*;

public class IRStoreInst extends IRInst {
  // store i32 3, ptr %ptr
  public IREntity src = null; // LocalVar ot Literal
  public IRVariable pos = null; // must be ptr type

  public IRStoreInst(IRBlock parent, IREntity src, IRVariable pos) {
    super(parent);
    this.src = src;
    this.pos = pos;
    if (!pos.getType().isPtr) throw new RuntimeException("Store: pos is not ptr");
  }

  public String toString() {
    return "store " + src.getType().toString() + " " + src.toString() + ", ptr " + pos.toString();
  }

  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
