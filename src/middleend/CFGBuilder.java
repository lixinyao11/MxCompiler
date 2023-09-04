package middleend;

import ir.IRBlock;
import ir.IRProgram;
import ir.inst.IRBrInst;
import ir.inst.IRInst;
import ir.inst.IRJumpInst;
import ir.inst.IRRetInst;
import ir.module.IRFuncDef;

public class CFGBuilder {
  IRProgram program = null;

  public CFGBuilder(IRProgram program) {
    this.program = program;
  }

  public void work() {
    program.functions.forEach((key, func) -> workOnFunc(func));
  }

  public void workOnFunc(IRFuncDef func) {
    func.body.forEach(this::workOnBlock);

    // 去除不是entry，也没有前驱的block
    func.body.forEach(block -> {
      if (block.preds.isEmpty() && !block.label.equals("entry")) {
        block.succs.forEach(succ -> succ.preds.remove(block));
        func.body.remove(block);
      }
    });
    func.body.removeIf(block -> block.preds.isEmpty() && !block.label.equals("entry"));
  }

  public void workOnBlock(IRBlock block) {
    IRInst exit = block.instructions.get(block.instructions.size() - 1);
    if (exit instanceof IRRetInst) {
      // is exitBlock, has no succ
      return;
    } else if (exit instanceof IRJumpInst) {
      IRBlock succ = ((IRJumpInst) exit).destBlock;
      block.succs.add(succ);
      succ.preds.add(block);
    } else if (exit instanceof IRBrInst) {
      IRBlock succ = ((IRBrInst) exit).thenBlock;
      block.succs.add(succ);
      succ.preds.add(block);

      succ = ((IRBrInst) exit).elseBlock;
      block.succs.add(succ);
      succ.preds.add(block);
    }
  }
}
