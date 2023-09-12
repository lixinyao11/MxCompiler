package asm.operand;

public class PhysicalRegister extends Register {
  String name;

  public PhysicalRegister(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }
}
