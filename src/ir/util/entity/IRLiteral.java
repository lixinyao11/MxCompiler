package ir.util.entity;

import ir.util.IRType;

public class IRLiteral extends IREntity {
  public String value = null; // "1" / "true"->"1" / "null" / "aa"
  // ! 没有"true"只有”1“
  public IRType type = null;

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
    if (type.isInt) return Integer.parseInt(value);
    else if (type.isPtr && value.equals("null")) return 0;
    else throw new RuntimeException("IRLiteral: getIntValue: not an int");
  }
}
