package ir.util.entity;

import ir.util.IRType;

public class GlobalPtr extends IRVariable {
  String name = null;

  public GlobalPtr(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "@" + name;
  }

  @Override
  public IRType getType() {
    return new IRType("ptr");
  }
}
