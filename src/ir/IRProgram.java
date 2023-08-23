package ir;

import ir.module.*;

import java.util.ArrayList;
import java.util.HashMap;

public class IRProgram {
  public ArrayList<IRFuncDecl> funcDecls = null;
  public HashMap<String, IRStructDef> structs = null; // 原class name，不带"class.
  public HashMap<String, IRFuncDef> functions = null;
  public HashMap<String, IRGlobalVarDef> globals = null;
  public ArrayList<IRStringLiteralDef> stringLiterals = null;
  public int stringLiteralCnt = 0;

  public IRProgram() {
    structs = new HashMap<>();
    functions = new HashMap<>();
    globals = new HashMap<>();
    stringLiterals = new ArrayList<>();
    funcDecls = new ArrayList<>();
    funcDecls.add(new IRFuncDecl("void", "print", "ptr"));
    funcDecls.add(new IRFuncDecl("ptr", "_string.concat", "ptr", "ptr"));
    funcDecls.add(new IRFuncDecl("ptr", "_string.copy", "ptr"));
    funcDecls.add(new IRFuncDecl("i32", "_string.compare", "ptr", "ptr"));
    funcDecls.add(new IRFuncDecl("ptr", "_malloc_array", "i32", "i32"));
    funcDecls.add(new IRFuncDecl("ptr", "_malloc", "i32"));
    funcDecls.add(new IRFuncDecl("void", "println", "ptr"));
    funcDecls.add(new IRFuncDecl("void", "printInt", "i32"));
    funcDecls.add(new IRFuncDecl("void", "printlnInt", "i32"));
    funcDecls.add(new IRFuncDecl("ptr", "getString", null));
    funcDecls.add(new IRFuncDecl("i32", "getInt", null));
    funcDecls.add(new IRFuncDecl("ptr", "toString", "i32"));
    funcDecls.add(new IRFuncDecl("i32", "_string.length", "ptr"));
    var SubStringFunc = new IRFuncDecl("ptr", "_string.substring", "ptr");
    SubStringFunc.addArgType("i32");
    SubStringFunc.addArgType("i32");
    funcDecls.add(SubStringFunc);

    funcDecls.add(new IRFuncDecl("i32", "_string.parseInt", "ptr"));
    var OrdFunc = new IRFuncDecl("i32", "_string.ord", "ptr");
    OrdFunc.addArgType("i32");
    funcDecls.add(OrdFunc);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (IRFuncDecl funcDecl : funcDecls) {
      sb.append(funcDecl.toString());
    }
    for (IRStructDef struct : structs.values()) {
      sb.append(struct.toString());
    }
    for (IRGlobalVarDef global : globals.values()) {
      sb.append(global.toString());
    }
    for (IRStringLiteralDef stringLiteral : stringLiterals) {
      sb.append(stringLiteral.toString());
    }
    for (IRFuncDef function : functions.values()) {
      sb.append(function.toString());
    }
    return sb.toString();
  }
}
