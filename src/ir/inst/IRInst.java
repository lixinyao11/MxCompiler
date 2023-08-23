package ir.inst;

import ir.IRBlock;
import ir.IRVisitor;

abstract public class IRInst {
  public IRBlock parent = null;
  abstract public String toString();

  abstract public void accept(IRVisitor visitor);
  public IRInst(IRBlock parent) {
    this.parent = parent;
  }
}
