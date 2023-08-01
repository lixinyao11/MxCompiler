package Util.Decl;

import Util.Type;
import java.util.HashMap;
import AST.*;

public class ClassDecl {
  String name;
  HashMap<String, Type> varDcls = null;
  HashMap<String, FuncDecl> funcDcls = null;
  
  public ClassDecl(String name) {
    this.name = name;
    this.varDcls = new HashMap<>();
    this.funcDcls = new HashMap<>();
  }

  public ClassDecl(ClassDefNode classDef) {
    this.name = classDef.name;
    this.varDcls = new HashMap<>();
    this.funcDcls = new HashMap<>();
    for (var varDef : classDef.varDef_list) {
      for (var i : varDef.varList) {
        varDcls.put(i.a, varDef.type);
      }
    }
    for (var funcDef : classDef.funcDef_list) {
      funcDcls.put(funcDef.name, new FuncDecl(funcDef));
    }
  }

  public void addFunc(FuncDefNode funcDef) {
    funcDcls.put(funcDef.name, new FuncDecl(funcDef));
  }

  public void addFunc(FuncDecl funcDecl) {
    funcDcls.put(funcDecl.name, funcDecl);
  }
}