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

    funcdcls.put("print", new FuncDecl("print", "void", "string", null));
    funcdcls.put("println", new FuncDecl("println", "void", "string", null));
    funcdcls.put("printInt", new FuncDecl("printInt", "void", "int", null));
    funcdcls.put("printlnInt", new FuncDecl("printlnInt", "void", "int", null));
    funcdcls.put("getString", new FuncDecl("getString", "string", null, null));
    funcdcls.put("getInt", new FuncDecl("getInt", "int", null, null));
    funcdcls.put("toString", new FuncDecl("toString", "string", "int", null));

    ClassDecl stringClass = new ClassDecl("string");
    stringClass.addFunc(new FuncDecl("length", "int", null, null));
    stringClass.addFunc(new FuncDecl("substring", "string", "int", "int"));
    stringClass.addFunc(new FuncDecl("parseInt", "int", null, null));
    stringClass.addFunc(new FuncDecl("ord", "int", "int", null));
  }
}
