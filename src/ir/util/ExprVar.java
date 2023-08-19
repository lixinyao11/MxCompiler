package ir.util;

import ir.util.entity.IREntity;
import ir.util.entity.IRVariable;

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
