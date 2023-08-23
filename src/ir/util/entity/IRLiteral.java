package ir.util.entity;

import ir.util.IRType;

public class IRLiteral extends IREntity {
  String value = null; // "1" / "true"->"1" / "null" / "aa"
  IRType type = null;

  public IRLiteral(String value, IRType type) {
    this.value = value;
    this.type = type;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public IRType getType() {
    return type;
  }

  public int getIntValue() {
    if (type.isInt && type.bit_width == 32) return Integer.parseInt(value);
    else if (type.isInt && type.bit_width == 1) return value.equals("true") ? 1 : 0;
    else if (type.isPtr && value.equals("null")) return 0;
    else throw new RuntimeException("IRLiteral: getIntValue: not an int");
  }
}
