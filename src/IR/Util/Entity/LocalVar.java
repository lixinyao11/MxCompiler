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
    return "%_" + name;
  }

  @Override
  public IRType getType() {
    return type;
  }

  public String getName() {
    return name;
  }
}
