package util.scope;

import java.util.HashMap;

import util.Position;
import util.SemanticError;
import ast.type.*;

public class Scope {
  public Scope parentScope = null;
  HashMap<String, VarType> varDefs = null;

  public Scope(Scope parent) {
    this.parentScope = parent;
    this.varDefs = new HashMap<>();
  }

  public void addVarSafe(VarType type, String name, Position pos) {
    if (varDefs.containsKey(name))
      throw new SemanticError("variable " + name + " redefined", pos);
    varDefs.put(name, type);
  }

  public boolean existVarName(String name) {
    if (varDefs.containsKey(name))
      return true;
    return false;
  }

  // public Type getVar(String name, Position pos) {
  //   if (varDefs.containsKey(name))
  //     return varDefs.get(name);
  //   if (parentScope != null)
  //     return parentScope.getVar(name, pos);
  //   throw new SemanticError("variable " + name + " not defined", pos);
  // }

  public void returnsType(ExprType type, Position pos) {
    parentScope.returnsType(type, pos);
  }

  public boolean isInLoop() {
    return parentScope.isInLoop();
  }

  public String isInClass() {
    return parentScope.isInClass();
  }

  public ExprType getIdType(String name) {
    if (varDefs.containsKey(name))
      return new ExprType(varDefs.get(name));
    if (parentScope != null)
      return parentScope.getIdType(name);
    return null;
  }
  
}
