package asm.inst;

import asm.operand.*;
import asm.section.ASMBlock;

import java.util.HashSet;

public class ASMLiInst extends ASMInst {
  Register rd = null;
  Immediate imm = null;

  public ASMLiInst(ASMBlock parent, Register rd, Immediate imm) {
    super(parent);
    this.rd = rd;
    this.imm = imm;
  }
  @Override
  public String toString() {
    return String.format("%-8s", "li") + rd + ", " + imm;
  }
  @Override
  public void initUseDef(HashSet<VirtualRegister> use, HashSet<VirtualRegister> def) {
    if (rd instanceof VirtualRegister)
      def.add((VirtualRegister) rd);
  }
}
