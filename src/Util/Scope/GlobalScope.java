package Util.Scope;

import Util.*;
import Util.SemanticError;
import Util.Decl.*;
import Util.Type.*;

public class GlobalScope extends BuiltinScope {

  public GlobalScope() {
    super();
  }

  public void addClassSafe(String name, ClassDecl classDecl, Position pos) {
    if (classdcls.containsKey(name))
      throw new SemanticError("class " + name + " redefined", pos);
    classdcls.put(name, classDecl);
  }

  public void addFuncSafe(String name, FuncDecl funcDecl, Position pos) {
    if (funcdcls.containsKey(name))
      throw new SemanticError("function " + name + " redefined", pos);
    funcdcls.put(name, funcDecl);
  }

  public FuncDecl getFuncDecl(String name) {
    if (!funcdcls.containsKey(name))
      return null;
    return funcdcls.get(name);
  }

  public ClassDecl getClassDecl(String name) {
    if (!classdcls.containsKey(name))
      return null;
    return classdcls.get(name);
  }

  @Override
  public boolean isInLoop() {
    return false;
  }

  @Override
  public void returnsType(ExprType type, Position pos) {
    throw new SemanticError("return statement not in a function", pos);
  }

  @Override
  public String isInClass() {
    return null;
  }

  @Override
  public ExprType getIdType(String name) {
    if (funcdcls.containsKey(name))
      return new ExprType(name, funcdcls.get(name));
    return super.getIdType(name);
  }
}
