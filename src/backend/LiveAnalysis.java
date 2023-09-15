package backend;

import asm.ASMProgram;
import asm.section.ASMBlock;

import java.util.ArrayList;

public class LiveAnalysis {
  ASMProgram program = null;
  ArrayList<Integer> startBlockIndex = null;

  public LiveAnalysis(ASMProgram program) {
    this.program = program;
    this.startBlockIndex = new ArrayList<>();
  }

  public void work() {
    program.blocks.forEach(ASMBlock::initUseDef);
    buildCFG();
    for (int i = 0; i < startBlockIndex.size(); ++i) {
      int st = startBlockIndex.get(i);
      int ed = (i < startBlockIndex.size() - 1) ? startBlockIndex.get(i + 1) : program.blocks.size();
      boolean changed = true;
      while (changed) {
        changed = false;
        for (int j = st; j < ed; ++j)
          changed = recompute(program.blocks.get(j));
      }
    }
  }
  private void buildCFG() {
    for (var block : program.blocks) {
      if (block.irBlock == null) {
        var index = program.blocks.indexOf(block);
        startBlockIndex.add(index);
        block.succs.add(program.blocks.get(index + 1));
        program.blocks.get(index + 1).preds.add(block);
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
