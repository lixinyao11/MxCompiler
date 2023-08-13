package IR.Inst;

public class Jump extends IRInst {
  public String destLabel = null;

  public Jump(String destLabel) {
    this.destLabel = destLabel;
  }

  public String toString() {
    return "br label %" + destLabel;
  }
}
