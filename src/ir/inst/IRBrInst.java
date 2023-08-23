package ir.inst;

import ir.IRBlock;
import ir.IRVisitor;
import ir.util.entity.LocalVar;

public class IRBrInst extends IRInst {
  public LocalVar cond = null; // must be i1
  public String thenLabel = null, elseLabel = null;

  public IRBrInst(IRBlock parent, LocalVar cond, String thenLabel, String elseLabel) {
    super(parent);
    this.cond = cond;
    this.thenLabel = thenLabel;
    this.elseLabel = elseLabel;
  }

  public String toString() {
    return "br i1 " + cond.toString() + ", label %" + thenLabel + ", label %" + elseLabel;
  }

  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
