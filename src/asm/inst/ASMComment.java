package asm.inst;

import asm.operand.*;
import asm.section.ASMBlock;

import java.util.HashSet;

public class ASMComment extends ASMInst {
  String comment;

  public ASMComment(ASMBlock parent, String comment) {
    super(parent);
    this.comment = comment;
  }

  @Override
  public String toString() {
    return "# " + comment;
  }
}
