package asm.inst;

import asm.operand.Register;
import asm.section.ASMBlock;

import java.util.HashSet;

public class ASMRetInst extends ASMInst {
  public ASMRetInst(ASMBlock parent) {
    super(parent);
  }
  @Override
  public String toString() {
    return String.format("%-8s", "ret");
  }
}
