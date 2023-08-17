package IR.Inst;

import IR.Util.Entity.*;
import IR.Util.IRType;

public class Alloca extends IRInst{
  // %a.1 = alloca i32 -> %a.1 is a ptr pointing to i32
  public IRVariable result = null;
  public IRType type = null;
  public String structName = null; // 不带"class."

  public Alloca(IRVariable result, IRType type) {
    this.result = result;
    this.type = type;
  }

  public Alloca(IRVariable result, String type) {
    this.result = result;
    this.structName = type;
  }

  public String toString() {
    if (structName != null) {
      if (type != null) throw new RuntimeException("Alloca has two types");
      return result.toString() + " = alloca %class." + structName;
    }
    return result.toString() + " = alloca " + type.toString();
  }
}
