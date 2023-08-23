package ir.module;

import ir.util.IRType;
import ir.IRVisitor;

import java.util.ArrayList;

public class IRFuncDecl extends IRModule {
  IRType retType = null;
  String name = null;
  ArrayList<IRType> argsType = null;

  public IRFuncDecl(String retType, String name, String arg1) {
    this.retType = new IRType(retType);
    this.name = name;
    this.argsType = new ArrayList<>();
    if (arg1 != null) argsType.add(new IRType(arg1));
  }

  public IRFuncDecl(String retType, String name, String arg1, String arg2) {
    this.retType = new IRType(retType);
    this.name = name;
    this.argsType = new ArrayList<>();
    if (arg1 != null) argsType.add(new IRType(arg1));
    if (arg2 != null) argsType.add(new IRType(arg2));
  }

  public void addArgType(String argType) {
    argsType.add(new IRType(argType));
  }
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("declare ").append(retType.toString()).append(" @").append(name).append("(");
    for (int i = 0; i < argsType.size(); ++i) {
      if (i != 0) sb.append(", ");
      sb.append(argsType.get(i).toString());
    }
    sb.append(")\n");
    return sb.toString();
  }
  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
