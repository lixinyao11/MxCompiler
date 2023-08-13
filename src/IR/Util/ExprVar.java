package IR.Util;

import IR.Util.Entity.IREntity;
import IR.Util.Entity.IRVariable;

public class ExprVar {
  public IREntity value = null;
  public IRVariable destptr = null;

  public ExprVar(IREntity value, IRVariable destptr) {
    this.value = value;
    this.destptr = destptr;
  }
}
