package IR.Util;

import IR.Util.Entity.IREntity;
import IR.Util.Entity.IRVariable;

public class ExprVar {
  public IREntity value = null;
  public IRVariable destptr = null;
  public FuncInfo funcInfo = null;

  public ExprVar(IREntity value, IRVariable destptr, FuncInfo funcInfo) {
    this.value = value;
    this.destptr = destptr;
    this.funcInfo = funcInfo;
  }
}
