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
      }
    });
    func.body.removeIf(block -> block.preds.isEmpty() && !block.label.equals("entry"));
  }

  public void workOnBlock(IRBlock block) {
    // ! 每个block的最后一条指令一定是exitInst，但中间也可能有exitInst(break, continue)
    // ! 如果中间出现了exitInst，那么后面的指令都是无效的，可以直接删去
    for (IRInst inst : block.instructions) {
      if (inst instanceof IRRetInst) {
        block.instructions.removeIf(tmp -> block.instructions.indexOf(tmp) > block.instructions.indexOf(inst));
        // is exitBlock, has no succ
        return;
      } else if (inst instanceof IRJumpInst) {
        IRBlock succ = ((IRJumpInst) inst).destBlock;
        block.succs.add(succ);
        succ.preds.add(block);
        block.instructions.removeIf(tmp -> block.instructions.indexOf(tmp) > block.instructions.indexOf(inst));
        return;
      } else if (inst instanceof IRBrInst) {
        IRBlock succ = ((IRBrInst) inst).thenBlock;
        block.succs.add(succ);
        succ.preds.add(block);

        succ = ((IRBrInst) inst).elseBlock;
        block.succs.add(succ);
        succ.preds.add(block);
        block.instructions.removeIf(tmp -> block.instructions.indexOf(tmp) > block.instructions.indexOf(inst));
        return;
      }
    }
  }
}
