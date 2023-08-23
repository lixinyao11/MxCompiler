package asm.section;

public class ASMStringLiteral extends ASMSection {
  String value = null; // without \"\"

  public ASMStringLiteral(String label, String value) {
    super(label);
    this.value = value;
  }

  @Override
  public String toString() {
    return "      .section rodata\n" +
            "      .globl " + label + "\n" +
            label + ":\n" +
            "      .asciz  \"" + value + "\"\n";
  }
}
