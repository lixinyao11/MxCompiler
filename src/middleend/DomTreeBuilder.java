package middleend;

import ir.IRBlock;
import ir.IRProgram;
import ir.module.IRFuncDef;

import java.util.ArrayList;

public class DomTreeBuilder {
  IRProgram program;

  public DomTreeBuilder(IRProgram program) {
    this.program = program;
  }

  public void work() {
    program.functions.forEach((key, func) -> workOnFunc(func));
  }

  public void workOnFunc(IRFuncDef func) {
    // get post order of blocks
    ArrayList<IRBlock> postOrder = new ArrayList<>();
    var entry = func.body.get(0);
    postTraverse(postOrder, entry);

    // get idom
    entry.idom = entry;
    boolean changed = true;
    while (changed) {
      changed = false;
      for (int i = postOrder.size() - 2; i >= 0; --i) { // in RPO, except entry
        var block = postOrder.get(i);
        IRBlock newIdom = null;
        for (var pred : block.preds) {
          if (pred.idom != null) {
            newIdom = intersect(pred, newIdom, postOrder);
          }
        }
        if (newIdom != block.idom) {
          block.idom = newIdom;
          changed = true;
        }
      }
    }

    // get domFrontier
    for (var block : postOrder) {
      if (block.preds.size() >= 2) {
        for (var pred : block.preds) {
          var runner = pred;
          while (runner != block.idom) { // runner doesn't dominate block
            runner.domFrontier.add(block); // block is in the domFrontier of runner
            runner = runner.idom; // 始终保证runner dominates pred
          }
        }
      }
    }
  }

  private IRBlock intersect(IRBlock b1, IRBlock b2, ArrayList<IRBlock> order) {
    if (b2 == null) {
      return b1;
    }
    // ! java中对象类型：值传递其地址，而不是引用传递
    // ! 修改b1，b2的值，不会影响实参
    while (b1 != b2) {
      while (order.indexOf(b1) < order.indexOf(b2))
        b1 = b1.idom;
      while (order.indexOf(b1) > order.indexOf(b2))
        b2 = b2.idom;
    }
    return b1;
  }

  private void postTraverse(ArrayList<IRBlock> postOrder, IRBlock block) {
    block.succs.forEach(succ -> {
      if (!postOrder.contains(succ))
        postTraverse(postOrder, succ);
    });
    postOrder.add(block);
  }
}
