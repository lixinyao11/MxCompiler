package IR;

import java.util.ArrayList;
import IR.Inst.*;

public class IRBlock {
  public IRFunction parent = null;
  public String label = null;
  public ArrayList<IRInst> instructions = null;

  public IRBlock(String label, IRFunction parent) {
    this.parent = parent;
    this.label = label;
    this.instructions = new ArrayList<>();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(label).append(":\n");
    for (IRInst inst : instructions) {
      sb.append("  ").append(inst.toString()).append("\n");
    }
    return sb.toString();
  }

  public void addInst(IRInst inst) {
    instructions.add(inst);
  }
  public void addInst(int index, IRInst inst) {
    instructions.add(index, inst);
  }
}
