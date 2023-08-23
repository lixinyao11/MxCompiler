package ir.inst;

import ir.IRBlock;
import ir.IRVisitor;
import ir.util.entity.*;

public class IRRetInst extends IRInst {
  public IREntity value = null;
  // localVar or literal or globalptr

  public IRRetInst(IRBlock parent, IREntity value) {
    super(parent);
    this.value = value;
  }

  public String toString() {
    if (value == null) return "ret void";
    return "ret " + value.getType().toString() + " " + value.toString();
  }

  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }

}