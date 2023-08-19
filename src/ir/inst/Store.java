package ir.inst;

import ir.util.entity.*;

public class Store extends IRInst {
  // store i32 3, ptr %ptr
  public IREntity src = null;
  public IRVariable pos = null; // must be ptr type

  public Store(IREntity src, IRVariable pos) {
    this.src = src;
    this.pos = pos;
    if (!pos.getType().isPtr) throw new RuntimeException("Store: pos is not ptr");
  }

  public String toString() {
    return "store " + src.getType().toString() + " " + src.toString() + ", ptr " + pos.toString();
  }
}
