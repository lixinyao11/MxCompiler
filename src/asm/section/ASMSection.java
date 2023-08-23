package asm.section;

public abstract class ASMSection {
  String label = null;
  public ASMSection(String label) {
    this.label = label;
  }

  abstract public String toString();
}
