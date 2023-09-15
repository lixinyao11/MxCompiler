package asm.inst;

import asm.operand.VirtualRegister;
import asm.section.ASMBlock;

import java.util.HashSet;

public class ASMJInst extends ASMInst {
  String label = null;

  public ASMJInst(ASMBlock parent, String label) {
    super(parent);
    this.label = label;
  }

  @Override
  public String toString() {
    return String.format("%-8s", "j") + label;
  }
  public void initUseDef(HashSet<VirtualRegister> use, HashSet<VirtualRegister> def) {}
}
