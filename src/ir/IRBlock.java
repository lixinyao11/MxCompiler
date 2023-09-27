package ir;

import java.util.*;

import asm.section.ASMBlock;
import ir.inst.*;
import ir.module.IRFuncDef;

public class IRBlock {
  public IRFuncDef parent = null;
  public String label = null;
  public ArrayList<IRInst> instructions = null;
  public HashSet<IRBlock> preds = null, succs = null; // * for CFG
  public IRBlock idom = null; // * for DomTree(father in DomTree)
  public ArrayList<IRBlock> domChildren = null; // * for DomTree
  public HashSet<IRBlock> domFrontier = null; // * for DomTree
  public HashMap<String, IRPhiInst> phiInsts = null; // * for Mem2Reg
  public ASMBlock asmBlock = null; // * for InstSelection
  public int loopDepth = 0;
//  public HashMap<String, LocalVar> defs = null, uses = null;

  public IRBlock(String label, IRFuncDef parent, int loopDepth) {
    this.parent = parent;
    this.label = label;
    this.instructions = new ArrayList<>();
    this.preds = new HashSet<>();
    this.succs = new HashSet<>();
    this.domFrontier = new HashSet<>();
    this.domChildren = new ArrayList<>();
    this.phiInsts = new HashMap<>();
    this.loopDepth = loopDepth;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(label).append(":\n");
    List<IRPhiInst> phis = new ArrayList<>(phiInsts.values());
    phis.sort(Comparator.comparing(IRPhiInst::toString));
    for (var phi : phis) {
      sb.append("  ").append(phi.toString()).append("\n");
    }
//    phiInsts.forEach((key, value) -> sb.append("  ").append(value.toString()).append("\n"));
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
