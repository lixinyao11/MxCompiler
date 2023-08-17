package IR;

import IR.Util.IRType;

import java.util.ArrayList;

public class StructDef {
  String name = null; // ! 原类名，不带"class."
  ArrayList<IRType> members = null;
  ArrayList<String> names = null;

  public StructDef(String name, ArrayList<IRType> members, ArrayList<String> names) {
    this.name = name;
    this.members = members;
    this.names = names;
  }

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

  public int getIndexOf(String var) {
    return names.indexOf(var);
  }
}
