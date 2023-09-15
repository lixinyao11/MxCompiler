package asm.inst;

import asm.operand.VirtualRegister;
import asm.section.ASMBlock;

import java.util.HashSet;

public abstract class ASMInst {
  public ASMBlock parent = null;
  public ASMInst(ASMBlock parent) {
    this.parent = parent;
  }
  abstract public String toString();
  abstract public void initUseDef(HashSet<VirtualRegister> use, HashSet<VirtualRegister> def);
}
