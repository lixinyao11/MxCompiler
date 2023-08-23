package asm.inst;

import asm.section.ASMBlock;

public class ASMRetInst extends ASMInst {

    public ASMRetInst(ASMBlock parent) {
      super(parent);
    }

    @Override
    public String toString() {
      return String.format("%-8s", "ret");
    }
}
