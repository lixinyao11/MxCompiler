package Util.Scope;

import java.util.HashMap;
import AST.*;
import Util.Decl.FuncDecl;

public class ClassScope extends Scope {
  HashMap<String, FuncDecl> funcdcls = null;
  
  public ClassScope(Scope parent) {
    super(parent);
    this.funcdcls = new HashMap<>();
  }

  public ClassScope(Scope parent, ClassDefNode classDef) {
    super(parent);
    this.funcdcls = new HashMap<>();
    for (var def : classDef.funcDef_list) {
      funcdcls.put(def.name, new FuncDecl(def));
    }
    for (var def : classDef.varDef_list) {
      for (var i : def.varList) {
        varDefs.put(i.a, def.type);
      }
    }
  }
}
