package ir;

import java.util.ArrayList;
import ir.inst.*;
import ir.module.IRFuncDef;

public class IRBlock {
  public IRFuncDef parent = null;
  public String label = null;
  public ArrayList<IRInst> instructions = null;

  public IRBlock(String label, IRFuncDef parent) {
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

  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
