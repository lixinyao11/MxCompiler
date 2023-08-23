package asm.operand;

public class Register {
  String name;

  public Register(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }
}
