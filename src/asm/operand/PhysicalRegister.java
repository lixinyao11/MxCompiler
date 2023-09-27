package asm.operand;

public class PhysicalRegister extends Register {
  public String name;
  static PhysicalRegister[] physicalRegisters = null;
  public PhysicalRegister(String name) {
    this.name = name;
  }
  public static PhysicalRegister get(int num) {
    return physicalRegisters[num];
  }
  public static PhysicalRegister get(String name) {
    if (physicalRegisters == null) {
      physicalRegisters = new PhysicalRegister[32];
      physicalRegisters[0] = new PhysicalRegister("zero");
      physicalRegisters[1] = new PhysicalRegister("ra");
      physicalRegisters[2] = new PhysicalRegister("sp");
      physicalRegisters[3] = new PhysicalRegister("gp");
      physicalRegisters[4] = new PhysicalRegister("tp");
      physicalRegisters[5] = new PhysicalRegister("t0");
      physicalRegisters[6] = new PhysicalRegister("t1");
      physicalRegisters[7] = new PhysicalRegister("t2");
      physicalRegisters[8] = new PhysicalRegister("s0");
      physicalRegisters[9] = new PhysicalRegister("s1");
      for (int i = 0; i < 8; ++i)
        physicalRegisters[i + 10] = new PhysicalRegister("a" + i);
      for (int i = 2; i < 12; ++i)
        physicalRegisters[i + 16] = new PhysicalRegister("s" + i);
      for (int i = 3; i < 7; ++i)
        physicalRegisters[i + 25] = new PhysicalRegister("t" + i);
    }
    return physicalRegisters[getNum(name)];
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
  private static int getNum(String name) {
    if (name.equals("ra")) return 1;
    if (name.equals("sp")) return 2;
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
  public boolean isCalleeSave() {
    return name.startsWith("s");
  }
}
