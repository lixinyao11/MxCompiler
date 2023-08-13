package IR.Util.Entity;

import IR.Util.IRType;

public class LocalVar extends IRVariable {
  String name = null;
  IRType type = null;

  public LocalVar(IRType type, String name) {
    this.name = name;
    this.type = type;
  }

  @Override
  public String toString() {
    return "%" + name;
  }

  @Override
  public IRType getType() {
    return type;
  }
}
