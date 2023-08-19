package util.decl;

import ast.type.*;
import java.util.ArrayList;
import ast.*;

public class FuncDecl {
  String name = null;
  ReturnType retType = null;
  ArrayList<VarType> paraList = null;

  // for builtin functions
  public FuncDecl(String name, String ret, String para1, String para2) {
    this.name = name;
    retType = new ReturnType(ret, 0);
    paraList = new ArrayList<>();
    if (para1 != null) paraList.add(new VarType(para1, 0));
    if (para2 != null) paraList.add(new VarType(para2, 0));
  }

  public FuncDecl(FuncDefNode funcDef) {
    name = funcDef.name;
    retType = funcDef.retType;
    paraList = new ArrayList<>();
    for (var para : funcDef.paraList.paras) {
      paraList.add(para.first);
    }
  }

  public boolean returnTypeIs(ReturnType type) {
    return retType.equals(type);
  }
  
  public ReturnType calledByArgs(ArrayList<ExprType> paras) {
    if (paraList.size() != paras.size())
      return null;
    for (int i = 0; i < paraList.size(); ++i) {
      if (!paraList.get(i).equals(paras.get(i)))
        return null;
    }
    return retType;
  }

  public ExprType getRetType() {
    return new ExprType(retType);
  }

}
