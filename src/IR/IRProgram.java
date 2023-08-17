package IR;

import Util.Decl.FuncDecl;

import java.util.ArrayList;
import java.util.HashMap;

public class IRProgram {
  public ArrayList<IRFuncDecl> funcDecls = null;
  public HashMap<String, StructDef> structs = null; // 原class name，不带"class.
  public HashMap<String, IRFunction> functions = null;
  public HashMap<String, GlobalVarDef> globals = null;
  public ArrayList<StringLiteralDef> stringLiterals = null;
  public int stringLiteralCnt = 0;

  public IRProgram() {
    structs = new HashMap<>();
    functions = new HashMap<>();
    globals = new HashMap<>();
    stringLiterals = new ArrayList<>();
    funcDecls = new ArrayList<>();
    funcDecls.add(new IRFuncDecl("void", "print", "ptr"));
    funcDecls.add(new IRFuncDecl("ptr", "_string_concat", "ptr", "ptr"));
    funcDecls.add(new IRFuncDecl("ptr", "_string_copy", "ptr"));
    funcDecls.add(new IRFuncDecl("i32", "_string_compare", "ptr", "ptr"));
    funcDecls.add(new IRFuncDecl("ptr", "_malloc_array", "i32", "i32"));
    funcDecls.add(new IRFuncDecl("void", "println", "ptr"));
    funcDecls.add(new IRFuncDecl("void", "printInt", "i32"));
    funcDecls.add(new IRFuncDecl("void", "printlnInt", "i32"));
    funcDecls.add(new IRFuncDecl("ptr", "getString", null));
    funcDecls.add(new IRFuncDecl("i32", "getInt", null));
    funcDecls.add(new IRFuncDecl("ptr", "toString", "i32"));
    funcDecls.add(new IRFuncDecl("i32", "_string_length", "ptr"));
    var SubStringFunc = new IRFuncDecl("ptr", "_string_substring", "ptr");
    SubStringFunc.addArgType("i32");
    SubStringFunc.addArgType("i32");
    funcDecls.add(SubStringFunc);

    funcDecls.add(new IRFuncDecl("i32", "_string_parseInt", "ptr"));
    var OrdFunc = new IRFuncDecl("i32", "_string_ord", "ptr");
    OrdFunc.addArgType("i32");
    funcDecls.add(OrdFunc);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (IRFuncDecl funcDecl : funcDecls) {
      sb.append(funcDecl.toString());
    }
    for (StructDef struct : structs.values()) {
      sb.append(struct.toString());
    }
    for (GlobalVarDef global : globals.values()) {
      sb.append(global.toString());
    }
    for (StringLiteralDef stringLiteral : stringLiterals) {
      sb.append(stringLiteral.toString());
    }
    for (IRFunction function : functions.values()) {
      sb.append(function.toString());
    }
    return sb.toString();
  }
}
