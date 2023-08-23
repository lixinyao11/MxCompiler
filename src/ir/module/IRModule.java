package ir.module;

import ir.IRVisitor;

public abstract class IRModule {
  abstract public String toString();
  abstract public void accept(IRVisitor visitor);
}
