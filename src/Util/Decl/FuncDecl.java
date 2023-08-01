package Util.Decl;

import Util.Type;
import java.util.ArrayList;
import AST.*;

public class FuncDecl {
  String name = null;
  boolean isVoid = false;
  Type retType = null;
  ArrayList<Type> paraList = null;

  public FuncDecl(String name, String retString) {
    this.name = name;
    if (retString.equals("void")) {
      isVoid = true;
    } else {
      retType = new Type(retString, 0);
    }
    paraList = new ArrayList<>();
  }

  public FuncDecl(String name, String ret, String para) {
    this.name = name;
    if (ret.equals("void")) {
      isVoid = true;
    } else {
      retType = new Type(ret, 0);
    }
    paraList = new ArrayList<>();
    paraList.add(new Type(para, 0));
  }

  public FuncDecl(String name, String ret, String para1, String para2) {
    this.name = name;
    if (ret.equals("void")) {
      isVoid = true;
    } else {
      retType = new Type(ret, 0);
    }
    paraList = new ArrayList<>();
    paraList.add(new Type(para1, 0));
    paraList.add(new Type(para2, 0));
  }

  public FuncDecl(FuncDefNode funcDef) {
    name = funcDef.name;
    if (funcDef.isVoid) {
      isVoid = true;
    } else {
      retType = funcDef.retType;
    }
    paraList = new ArrayList<>();
    for (var para : funcDef.paraList.paras) {
      paraList.add(para.a);
    }
  }

  public boolean returnsType(Type type) {
    if (isVoid) {
      if (type == null)
        return true;
      return false;
    }
    if (retType.equals(type))
      return true;
    return false;
  }
  
  public boolean checkParas(ArrayList<Type> paras) {
    if (paraList.size() != paras.size())
      return false;
    for (int i = 0; i < paraList.size(); ++i) {
      if (!paraList.get(i).equals(paras.get(i)))
        return false;
    }
    return true;
  }

}
