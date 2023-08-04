package Util.Scope;

import Util.Position;
import Util.SemanticError;
import Util.Type.*;

public class FuncScope extends Scope {
  ReturnType returnType = null;
  boolean isReturned = false;

  public FuncScope(Scope parent, ReturnType returnType) {
    super(parent);
    this.returnType = returnType;
    if (returnType.equals(new ReturnType("void", 0)))
      this.isReturned = true;
  }

  @Override
  public void returnsType(ExprType type, Position pos) {
    if (!returnType.equals(type))
      throw new SemanticError("function should return " + returnType.toString() + " but got " + type.toString(), pos);
    this.isReturned = true;
  }

  public boolean isReturned() {
    return isReturned;
  }
  
}
