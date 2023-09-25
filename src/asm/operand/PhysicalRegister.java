package asm.operand;

public class PhysicalRegister extends Register {
  public String name;

  public PhysicalRegister(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }
  public Integer getColor() {
    if (name.equals("ra")) throw new RuntimeException("PhysicalRegister.getColor: ra is not a color");
    if (name.equals("sp")) throw new RuntimeException("PhysicalRegister.getColor: sp is not a color");
    if (name.startsWith("a")) return Integer.parseInt(name.substring(1)) + 10;
    if (name.equals("t0")) return 5;
    if (name.equals("t1")) return 6;
    if (name.equals("t2")) return 7;
    if (name.equals("s0")) return 8;
    if (name.equals("s1")) return 9;
    if (name.startsWith("s")) return Integer.parseInt(name.substring(1)) + 16;
    if (name.startsWith("t")) return Integer.parseInt(name.substring(1)) + 25;
    throw new RuntimeException("PhysicalRegister.getColor: unknown register name");
  }
}
