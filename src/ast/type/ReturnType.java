package ast.type;

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
    if (obj instanceof ExprType tmp) {
      if (tmp.isNull) {
        if (isVoid) return false;
        return dim > 0;
      }
    }
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
