package ir;

import java.util.ArrayList;
import java.util.HashSet;

import ir.inst.*;
import ir.module.IRFuncDef;

public class IRBlock {
  public IRFuncDef parent = null;
  public String label = null;
  public ArrayList<IRInst> instructions = null;
  public HashSet<IRBlock> preds = null, succs = null; // * for CFG
  public IRBlock idom = null; // * for DomTree
  public HashSet<IRBlock> domFrontier = null; // * for DomTree
//  public HashMap<String, LocalVar> defs = null, uses = null;

  public IRBlock(String label, IRFuncDef parent) {
    this.parent = parent;
    this.label = label;
    this.instructions = new ArrayList<>();
    this.preds = new HashSet<>();
    this.succs = new HashSet<>();
    this.domFrontier = new HashSet<>();
//    this.defs = new HashMap<>();
//    this.uses = new HashMap<>();
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
