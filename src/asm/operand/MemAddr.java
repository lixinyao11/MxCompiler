package asm.operand;

public class MemAddr {
  Immediate offset;
  Register base;

  public MemAddr(Immediate offset, Register base) {
    this.offset = offset;
    this.base = base;
  }

  public String toString() {
    return offset.toString() + "(" + base.toString() + ")";
  }
}
