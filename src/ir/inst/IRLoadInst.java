package ir.inst;

import ir.IRBlock;
import ir.IRVisitor;
import ir.util.entity.*;
import ir.util.IRType;

public class IRLoadInst extends IRInst {
  // %result = load <type>, ptr %ptr
  public LocalVar result = null;
  public IRVariable ptr = null; // must be ptr type
  // globalPtr or LocalVar

  public IRLoadInst(IRBlock parent, LocalVar result, IRVariable ptr) {
    super(parent);
    if (!ptr.getType().equals(new IRType("ptr")))
      throw new RuntimeException("ptr is not ptr type in load");
    this.result = result;
    this.ptr = ptr;
  }

  public String toString() {
    return result.toString() + " = load " + result.getType().toString() + ", ptr " + ptr.toString();
  }

  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
