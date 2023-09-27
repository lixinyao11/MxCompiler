package asm.inst;

import asm.operand.*;
import asm.section.ASMBlock;

import java.util.HashSet;

public class ASMCallInst extends ASMInst {
  Label label = null;
  public HashSet<Register> live = null;

  public ASMCallInst(ASMBlock parent, Label label) {
    super(parent);
    this.label = label;
  }

  @Override
  public String toString() {
    return String.format("%-8s", "call") + label;
  }
}
