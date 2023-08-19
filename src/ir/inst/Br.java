package ir.inst;

import ir.util.entity.LocalVar;

public class Br extends IRInst {
  public LocalVar cond = null; // must be i1
  public String thenLabel = null, elseLabel = null;

  public Br(LocalVar cond, String thenLabel, String elseLabel) {
    this.cond = cond;
    this.thenLabel = thenLabel;
    this.elseLabel = elseLabel;
  }

  public String toString() {
    return "br i1 " + cond.toString() + ", label %" + thenLabel + ", label %" + elseLabel;
  }
}
