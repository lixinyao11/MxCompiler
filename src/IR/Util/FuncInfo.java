package IR.Util;

import IR.Util.Entity.IRVariable;
import IR.Util.Entity.LocalVar;

public class FuncInfo {
  public String name; // with "A::"
  public IRType retType = null;
  public IRVariable thisptr = null; // must be ptr type

  public FuncInfo(String name, IRType retType, IRVariable thisptr) {
    if (thisptr != null && !thisptr.getType().equals(new IRType("ptr")))
      throw new RuntimeException("thisptr is not ptr in funcinfo");
    this.name = name;
    this.retType = retType;
    this.thisptr = thisptr;
  }
}
