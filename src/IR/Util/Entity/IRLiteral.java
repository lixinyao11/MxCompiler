package IR.Util.Entity;

import IR.Util.IRType;

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
}
