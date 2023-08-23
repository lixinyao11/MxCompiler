package ir.module;

import ir.util.entity.*;
import ir.util.*;
import ir.*;

public class IRGlobalVarDef extends IRModule {
  public GlobalPtr var = null;
  public IRLiteral init = null; // type is the storage var points to

  public IRGlobalVarDef(GlobalPtr var, String init, IRType type) {
    this.var = var;
    if (init == null) {
      if (type.isPtr) init = "null";
      else init = "0";
    }
    this.init = new IRLiteral(init, type);
  }
  @Override
  public String toString() {
    return var.toString() + " = global " + init.getType().toString() + " " + init.toString() + "\n";
  }
  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }

}
