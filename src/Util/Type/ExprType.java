package Util.Type;

import Util.Decl.*;

public class ExprType extends ReturnType {
  public boolean isNull = false, isFunc = false;
  public FuncDecl funcDecl = null;
  public ExprType(String baseType, int dim) {
    super(baseType, dim);
    if (baseType.equals("null")) {
      this.isNull = true;
      this.isClass = false;
      this.dim = 0;
    }
  }
  // is Function
  public ExprType(String name, FuncDecl funcDecl) {
    super(name, 0);
    this.isFunc = true;
    this.funcDecl = funcDecl;
    this.isClass = false;
  }
  public ExprType(BaseType type) {
    super(type);
    if (type instanceof ExprType) {
      this.isNull = ((ExprType) type).isNull;
      this.isFunc = ((ExprType) type).isFunc;
      this.funcDecl = ((ExprType) type).funcDecl;
    }
  }

  // null is equal to class/array/null
  // function is equal to nothing
  @Override
  public boolean equals(BaseType obj) {
    ExprType other = new ExprType(obj);
    if (isFunc || other.isFunc) return false;
    if (isNull) return other.isClass || other.dim > 0 || other.isNull;
    if (other.isNull) return isClass || dim > 0;
    return super.equals(obj);
  }

  @Override
  public String toString() {
    if (isFunc)
      return "function" + name;
    if (isNull)
      return "null";
    return super.toString();
  }
  
}
