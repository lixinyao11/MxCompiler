package IR;

import java.util.HashMap;

public class IRProgram {
  public HashMap<String, StructDef> structs = null; // 原class name，不带"class.
  public HashMap<String, IRFunction> functions = null;
  public HashMap<String, GlobalVarDef> globals = null;

  public IRProgram() {
    structs = new HashMap<>();
    functions = new HashMap<>();
    globals = new HashMap<>();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (StructDef struct : structs.values()) {
      sb.append(struct.toString());
    }
    for (GlobalVarDef global : globals.values()) {
      sb.append(global.toString());
    }
    for (IRFunction function : functions.values()) {
      sb.append(function.toString());
    }
    return sb.toString();
  }
}
