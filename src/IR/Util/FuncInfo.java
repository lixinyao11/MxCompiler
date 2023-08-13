package IR.Util;

import IR.Util.Entity.LocalVar;

public class FuncInfo {
  public String name; // with "A::"
  public IRType retType = null;
  public LocalVar thisptr = null;
}
