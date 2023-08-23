package asm.operand;

public class Immediate {
  int value = 0; // ? use string for storage?
  String globalSymbol = null;

  public Immediate(int value) {
    this.value = value;
  }
  public Immediate(String globalSymbol) {
    this.globalSymbol = globalSymbol;
  }

  @Override
  public String toString() {
    if (globalSymbol != null)
      return globalSymbol;
    return Integer.toString(value);
  }
}
