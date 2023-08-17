package IR;

import IR.Util.Entity.GlobalPtr;
import IR.Util.Entity.IRLiteral;
import IR.Util.IRType;

public class GlobalVarDef {
  GlobalPtr var = null;
  IRLiteral init = null; // type is the storage var points to

  public GlobalVarDef(GlobalPtr var, String init, IRType type) {
    this.var = var;
    if (init == null) {
      if (type.isPtr) init = "null";
      else init = "0";
    }
    this.init = new IRLiteral(init, type);
  }

  public String toString() {
    return var.toString() + " = global " + init.getType().toString() + " " + init.toString() + "\n";
  }

}
