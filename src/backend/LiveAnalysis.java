package backend;

import asm.section.*;

public class LiveAnalysis {
  public void workOnFunc(ASMFunction func) {
    func.blocks.forEach(ASMBlock::initUseDef);
    buildCFG(func);
    boolean changed = true;
    while (changed) {
      changed = false;
      for (var block : func.blocks)
        changed = recompute(block);
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
    var save = block.liveOut;
    for (var succ : block.succs)
      block.liveOut.addAll(succ.liveIn);
    block.liveIn = block.use;
    for (var out : block.liveOut) {
      if (!block.def.contains(out))
        block.liveIn.add(out);
    }
    return !save.equals(block.liveOut);
  }
}
