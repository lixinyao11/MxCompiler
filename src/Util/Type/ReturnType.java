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

  public ReturnType(BaseType type) {
    super(type);
    if (type instanceof ReturnType) {
      this.isVoid = ((ReturnType) type).isVoid;
    }
  }

  // void is equal to void only
  @Override
  public boolean equals(BaseType obj) {
    ReturnType other = new ReturnType(obj);
    if (isVoid && other.isVoid) return true;
    return !isVoid && !other.isVoid && super.equals(obj);
  }

  @Override
  public String toString() {
    if (isVoid)
      return "void";
    return super.toString();
  }
}
