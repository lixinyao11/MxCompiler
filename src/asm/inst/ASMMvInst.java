package asm.inst;

import asm.operand.Register;
import asm.operand.VirtualRegister;
import asm.section.ASMBlock;

import java.util.HashSet;

public class ASMMvInst extends ASMInst {
  Register rd = null, rs = null;

  public ASMMvInst(ASMBlock parent, Register rd, Register rs) {
    super(parent);
    this.rd = rd;
    this.rs = rs;
  }

  @Override
  public String toString() {
    return String.format("%-8s", "mv") + rd + ", " + rs;
  }
  @Override
  public void initUseDef(HashSet<VirtualRegister> use, HashSet<VirtualRegister> def) {
    if (rs instanceof VirtualRegister && !def.contains(rs))
      use.add((VirtualRegister) rs);
    if (rd instanceof VirtualRegister)
      def.add((VirtualRegister) rd);
  }
}
