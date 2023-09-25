package asm.inst;

import asm.operand.Register;
import asm.section.ASMBlock;

import java.util.ArrayList;

public abstract class ASMInst {
  public ASMBlock parent = null;
  public ASMInst(ASMBlock parent) {
    this.parent = parent;
  }
  abstract public String toString();
  public Register def() {
    return null;
  }
  public void setDef(Register reg) {
    throw new RuntimeException("inst has no def");
  }
  public Register use1() {
    return null;
  }
  public void setUse1(Register reg) {
    throw new RuntimeException("inst has no use1");
  }
  public Register use2() {
    return null;
  }
  public void setUse2(Register reg) {
    throw new RuntimeException("inst has no use2");
  }
  public ArrayList<Register> getRegs() {
    var ret = new ArrayList<Register>();
    if (def() != null) ret.add(def());
    if (use1() != null) ret.add(use1());
    if (use2() != null) ret.add(use2());
    return ret;
  }
}
