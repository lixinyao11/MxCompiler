package ir.module;

import ir.util.IRType;
import ir.*;

import java.util.ArrayList;

public class IRStructDef extends IRModule {
  public String name = null; // ! 原类名，不带"class."
  public ArrayList<IRType> members = null;
  ArrayList<String> names = null;

  public IRStructDef(String name, ArrayList<IRType> members, ArrayList<String> names) {
    this.name = name;
    this.members = members;
    this.names = names;
  }
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("%class.").append(name).append(" = type { ");
    for (int i = 0; i < members.size(); ++i) {
      if (i != 0) sb.append(", ");
      sb.append(members.get(i).toString());
    }
    sb.append(" }\n");
    return sb.toString();
  }
  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }

  public int getIndexOf(String var) {
    return names.indexOf(var);
  }
}
