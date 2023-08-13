package Util.Scope;

import java.util.HashMap;
import AST.*;
import Util.Decl.FuncDecl;
import Util.*;
import Util.Type.ExprType;

public class ClassScope extends Scope {
  HashMap<String, FuncDecl> funcdcls = null;
  String className = null;

  public ClassScope(Scope parent, ClassDefNode classDef) {
    super(parent);
    this.className = classDef.name;
    this.funcdcls = new HashMap<>();
    for (var def : classDef.funcDef_list) {
      if (funcdcls.containsKey(def.name))
        throw new SemanticError("ClassScope: " + classDef.name + "duplicated function name", def.pos);
      funcdcls.put(def.name, new FuncDecl(def));
    }
    for (var def : classDef.varDef_list) {
      for (var i : def.varList) {
        if (varDefs.containsKey(i.first))
          throw new SemanticError("ClassScope: " + classDef.name + "duplicated variable name", def.pos);
        varDefs.put(i.first, def.type);
      }
    }
  }

  @Override
  public String isInClass() {
    return className;
  }

  @Override
  public ExprType getIdType(String name) {
    if (funcdcls.containsKey(name))
      return new ExprType(name, funcdcls.get(name));
    return super.getIdType(name);
  }
}
