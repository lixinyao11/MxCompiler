package ir.inst;

import ir.IRBlock;
import ir.IRVisitor;

public class IRJumpInst extends IRInst {
  public String destLabel = null;

  public IRJumpInst(IRBlock parent, String destLabel) {
    super(parent);
    this.destLabel = destLabel;
  }

  public String toString() {
    return "br label %" + destLabel;
  }

  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
