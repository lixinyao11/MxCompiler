package backend;

import asm.operand.Register;
import asm.section.*;

import java.util.HashSet;

public class LiveAnalysis {
  public void workOnFunc(ASMFunction func) {
    func.blocks.forEach(ASMBlock::initUseDef);
    buildCFG(func);
    boolean changed = true;
    while (changed) {
      changed = false;
      for (var block : func.blocks)
        changed |= recompute(block);
    }
  }
  private void buildCFG(ASMFunction func) {
    for (var block : func.blocks) {
      if (func.blocks.indexOf(block) == 0) {
        block.succs.add(func.blocks.get(1));
        func.blocks.get(1).preds.add(block);
      } else {
        for (var tmp : block.irBlock.preds)
          block.preds.add(tmp.asmBlock);
        for (var tmp : block.irBlock.succs)
          block.succs.add(tmp.asmBlock);
      }
    }
  }
  private boolean recompute(ASMBlock block) {
    var save = new HashSet<>(block.liveOut);
    var saveIn = new HashSet<>(block.liveIn);
    for (var succ : block.succs)
      block.liveOut.addAll(succ.liveIn);
    block.liveIn = new HashSet<>(block.use);
    for (var out : block.liveOut) {
      if (isliveIn(block, out))
        block.liveIn.add(out);
    }
    return !save.equals(block.liveOut) || !saveIn.equals(block.liveIn);
  }
  private boolean isliveIn(ASMBlock block, Register reg) {
    for (var inst : block.insts) {
      if (inst.def() != null && inst.def().equals(reg))
        return false;
      if (inst.use1() != null && inst.use1().equals(reg))
        return true;
      if (inst.use2() != null && inst.use2().equals(reg))
        return true;
    }
    return true;
  }
}
