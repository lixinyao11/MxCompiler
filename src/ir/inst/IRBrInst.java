package ir.inst;

import ir.IRBlock;
import ir.IRVisitor;
import ir.util.entity.LocalVar;

public class IRBrInst extends IRInst {
  public LocalVar cond = null; // must be i1
  public IRBlock thenBlock = null, elseBlock = null;

  public IRBrInst(IRBlock parent, LocalVar cond, IRBlock thenBlock, IRBlock elseBlock) {
    super(parent);
    this.cond = cond;
    this.thenBlock = thenBlock;
    this.elseBlock = elseBlock;
  }

  public String toString() {
    return "br i1 " + cond.toString() + ", label %" + thenBlock.label + ", label %" + elseBlock.label;
  }

  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
