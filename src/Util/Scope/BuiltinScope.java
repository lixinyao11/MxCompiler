package Util.Scope;

import Util.Decl.*;
import java.util.HashMap;

public class BuiltinScope extends Scope {
  HashMap<String, FuncDecl> funcdcls = null;
  HashMap<String, ClassDecl> classdcls = null;

  public BuiltinScope() {
    super(null);
    this.funcdcls = new HashMap<>();
    this.classdcls = new HashMap<>();
    // todo: add builtin functions
    classdcls.put("bool", new ClassDecl("bool"));
    classdcls.put("int", new ClassDecl("int"));

    funcdcls.put("print", new FuncDecl("print", "void", "string"));
    funcdcls.put("println", new FuncDecl("println", "void", "string"));
    funcdcls.put("printInt", new FuncDecl("printInt", "void", "int"));
    funcdcls.put("printlnInt", new FuncDecl("printlnInt", "void", "int"));
    funcdcls.put("getString", new FuncDecl("getString", "string"));
    funcdcls.put("getInt", new FuncDecl("getInt", "int"));
    funcdcls.put("toString", new FuncDecl("toString", "string", "int"));

    ClassDecl stringClass = new ClassDecl("string");
    stringClass.addFunc(new FuncDecl("length", "int"));
    stringClass.addFunc(new FuncDecl("substring", "string", "int", "int"));
    stringClass.addFunc(new FuncDecl("parseInt", "int"));
    stringClass.addFunc(new FuncDecl("ord", "int", "int"));
  }
}
