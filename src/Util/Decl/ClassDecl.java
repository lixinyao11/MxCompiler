package Util.Decl;

import AST.Type.*;

import java.util.HashMap;
import AST.*;

public class ClassDecl {
  String name;
  HashMap<String, VarType> varDcls = null;
  HashMap<String, FuncDecl> funcDcls = null;
  boolean hasBuildFunc = false;
  int size = 0; // for ir
  
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
        varDcls.put(i.first, varDef.type);
      }
    }
    for (var funcDef : classDef.funcDef_list) {
      funcDcls.put(funcDef.name, new FuncDecl(funcDef));
    }
    if (classDef.classBuild != null) {
      hasBuildFunc = true;
    }
  }

  public void addFunc(FuncDefNode funcDef) {
    funcDcls.put(funcDef.name, new FuncDecl(funcDef));
  }

  public void addFunc(FuncDecl funcDecl) {
    funcDcls.put(funcDecl.name, funcDecl);
  }

  public FuncDecl getFunc(String name) {
    if (!funcDcls.containsKey(name))
      return null;
    return funcDcls.get(name);
  }

  public VarType getVar(String name) {
    if (!varDcls.containsKey(name))
      return null;
    return varDcls.get(name);
  }

  public boolean hasBuildFunc() {
    return hasBuildFunc;
  }

  public void setSize(int size) {
    this.size = size;
  }
  public int getSize() {
    return size;
  }

}
