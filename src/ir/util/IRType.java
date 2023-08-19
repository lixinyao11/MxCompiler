package ir.util;

public class IRType {
  public boolean isInt = false, isPtr = false, isVoid = false;
  public int bit_width = 0;

  public IRType(String type) {
    if (type.equals("ptr")) isPtr = true;
    else if (type.equals("void")) isVoid = true;
    else {
      isInt = true;
      bit_width = Integer.parseInt(type.substring(1));
    }
  }

  public String toString() {
    if (isInt) return "i" + String.valueOf(bit_width);
    else if (isPtr) return "ptr";
    else if (isVoid) return "void";
    else throw new RuntimeException("IRType.toString() error");
  }

  public boolean equals(IRType other) {
    return isInt == other.isInt && isPtr == other.isPtr && isVoid == other.isVoid && bit_width == other.bit_width;
  }
}
