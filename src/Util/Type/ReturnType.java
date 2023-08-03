package Util.Type;

import Util.*;

public class ReturnType extends VarType {
  public boolean isVoid = false;

  public ReturnType(String baseType, int dim) {
    super(baseType, dim);
    if (baseType.equals("void")) {
      this.isVoid = true;
    }
  }

  public ReturnType(ExprType type, Position pos) {
    super(type);
    if (type.isNull)
      throw new SemanticError("null type cannot be a return type", pos);
  }
  
  // void is equal to void only
  @Override
  public boolean equals(BaseType obj) {
    if (!(obj instanceof ReturnType)) {
      if (!isVoid && super.equals(obj))
        return true;
      return false;
    }
    if (isVoid && ((ReturnType) obj).isVoid)
      return true;
    return !isVoid && !((ReturnType) obj).isVoid && super.equals(obj);
  }
}
