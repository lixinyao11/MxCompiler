package IR;

import IR.Util.Entity.GlobalPtr;

public class StringLiteralDef {
  GlobalPtr ptr = null; // string.0, string.1...
  String value = null;

  public StringLiteralDef(GlobalPtr ptr, String value) {
    this.ptr = ptr;
    this.value = value;
  }

  public String toString() {
    return ptr.toString() + " = constant [" + (value.length() + 1) + " x i8] c\"" + value + "\\00\"\n";
  }
}
