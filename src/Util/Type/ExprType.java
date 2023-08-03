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

  // null is equal to any type except string and void
  // function is equal to nothing
  @Override
  public boolean equals(BaseType obj) {
    if (!(obj instanceof ExprType)) {
      if (obj instanceof ReturnType) {
        if (isNull && !obj.isString && !((ReturnType) obj).isVoid)
          return true;
      } else {
        if (isNull && !obj.isString)
          return true;
      }

      if (!isNull && !isFunc && super.equals(obj))
        return true;

      return false;
    }
    if (isFunc || ((ExprType) obj).isFunc)
      return false;
    if (isNull)
      return !((ExprType) obj).isString && !((ExprType) obj).isVoid;
    if (((ExprType) obj).isNull)
      return !isString && !isVoid;
    return super.equals(obj);
  }
  
}
