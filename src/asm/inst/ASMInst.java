package asm.inst;

import asm.section.ASMBlock;

public abstract class ASMInst {
  public ASMBlock parent = null;
  public ASMInst(ASMBlock parent) {
    this.parent = parent;
  }
  abstract public String toString();
}
