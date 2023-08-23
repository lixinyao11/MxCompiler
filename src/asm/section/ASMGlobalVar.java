package asm.section;

public class ASMGlobalVar extends ASMSection {
  int value = 0;

  public ASMGlobalVar(String label, int value) {
    super(label);
    this.value = value;
  }

  @Override
  public String toString() {
    return "      .section data\n" +
            "      .globl " + label + "\n" +
            label + ":\n" +
            "      .word  " + value + "\n";
  }
}
