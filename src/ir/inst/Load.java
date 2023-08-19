package ir.inst;

import ir.util.entity.*;
import ir.util.IRType;

public class Load extends IRInst {
  // %result = load <type>, ptr %ptr
  public LocalVar result = null;
  public IRVariable ptr = null; // must be ptr type

  public Load(LocalVar result, IRVariable ptr) {
    if (!ptr.getType().equals(new IRType("ptr")))
      throw new RuntimeException("ptr is not ptr type in load");
    this.result = result;
    this.ptr = ptr;
  }

  public String toString() {
    return result.toString() + " = load " + result.getType().toString() + ", ptr " + ptr.toString();
  }
}
