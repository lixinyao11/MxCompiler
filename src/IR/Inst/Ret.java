package IR.Inst;

import IR.Util.Entity.*;

public class Ret extends IRInst {
  IREntity value = null;

  public Ret(IREntity value) {
    this.value = value;
  }

  public String toString() {
    if (value == null) return "ret void";
    return "ret " + value.getType().toString() + " " + value.toString();
  }

}