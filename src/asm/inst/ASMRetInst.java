package asm.inst;

import asm.operand.VirtualRegister;
import asm.section.ASMBlock;

import java.util.HashSet;

public class ASMRetInst extends ASMInst {
  public ASMRetInst(ASMBlock parent) {
    super(parent);
  }
  @Override
  public void initUseDef(HashSet<VirtualRegister> use, HashSet<VirtualRegister> def) {}
  @Override
  public String toString() {
    return String.format("%-8s", "ret");
  }
}
