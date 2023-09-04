package ir.inst;

import ir.IRBlock;
import ir.IRVisitor;

public class IRJumpInst extends IRInst {
  public IRBlock destBlock = null;

  public IRJumpInst(IRBlock parent, IRBlock destBlock) {
    super(parent);
    this.destBlock = destBlock;
  }

  public String toString() {
    return "br label %" + destBlock.label;
  }

  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
