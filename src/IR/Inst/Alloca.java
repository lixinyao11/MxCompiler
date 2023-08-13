package IR.Inst;

import IR.Util.Entity.*;
import IR.Util.IRType;

public class Alloca extends IRInst{
  // %a.1 = alloca i32 -> %a.1 is a ptr pointing to i32
  public LocalPtr result = null;
  public IRType type = null;

  public Alloca(String result, IRType type) {
    this.result = new LocalPtr(result);
    this.type = type;
  }

  public String toString() {
    return result.toString() + " = alloca " + type.toString();
  }
}
