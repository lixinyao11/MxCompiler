package IR;

import IR.Util.Entity.GlobalPtr;

public class StringLiteralDef {
  GlobalPtr ptr = null; // string.0, string.1...
  String value = null;

  public StringLiteralDef(GlobalPtr ptr, String value) {
    this.ptr = ptr;
    this.value = value;
  }

  public String printStr() {
    StringBuilder ret = new StringBuilder();
    for (int i = 0; i < value.length(); ++i) {
      char c = value.charAt(i);
      switch (c) {
        case '\n' -> ret.append("\\0A");
        case '\"' -> ret.append("\\22");
        case '\\' -> ret.append("\\\\");
        default -> ret.append(c);
      }
    }
    return ret.append("\\00").toString();
  }

  public String toString() {
    return ptr.toString() + " = constant [" + (value.length() + 1) + " x i8] c\"" + printStr() + "\"\n";
  }
}
