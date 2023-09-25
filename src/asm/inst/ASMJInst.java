package asm.inst;

import asm.operand.Register;
import asm.section.ASMBlock;

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
}
