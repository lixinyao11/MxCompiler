package asm.inst;

import asm.operand.Label;
import asm.section.ASMBlock;

public class ASMCallInst extends ASMInst {
  Label label = null;

  public ASMCallInst(ASMBlock parent, Label label) {
    super(parent);
    this.label = label;
  }

  @Override
  public String toString() {
    return String.format("%-8s", "call") + label;
  }
}
