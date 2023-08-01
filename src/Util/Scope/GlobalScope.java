package Util.Scope;

import Util.Decl.*;

public class GlobalScope extends BuiltinScope {

  public GlobalScope() {
    super();
  }

  public void addClass(String name, ClassDecl classDecl) {
    classdcls.put(name, classDecl);
  }
  public void addFunc(String name, FuncDecl funcDecl) {
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

}
