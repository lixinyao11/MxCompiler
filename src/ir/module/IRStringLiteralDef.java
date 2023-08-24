package ir.module;

import ir.util.entity.GlobalPtr;
import ir.*;

public class IRStringLiteralDef extends IRModule {
  public GlobalPtr ptr = null; // string.0, string.1...
  public String value = null, orgValue = null;

  public IRStringLiteralDef(GlobalPtr ptr, String value, String orgValue) {
    this.ptr = ptr;
    this.value = value;
    this.orgValue = orgValue;
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
  @Override
  public String toString() {
    return ptr.toString() + " = constant [" + (value.length() + 1) + " x i8] c\"" + printStr() + "\"\n";
  }
  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
