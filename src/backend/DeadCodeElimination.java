package backend;

import asm.*;
import asm.inst.ASMInst;
import asm.operand.VirtualRegister;
import asm.section.ASMBlock;
import asm.section.ASMFunction;

import java.util.ArrayList;
import java.util.HashSet;

public class DeadCodeElimination {
  ASMProgram program;

  public DeadCodeElimination(ASMProgram program) {
    this.program = program;
  }
  public void work() {
    program.functions.forEach(this::workOnFunc);
  }
  public void workOnFunc(ASMFunction func) {
    new LiveAnalysis().workOnFunc(func);
    for (int i = func.blocks.size() - 1; i >= 0; i--) {
      var block = func.blocks.get(i);
      var save = new HashSet<>(block.liveOut);
      ArrayList<ASMInst> newInsts = new ArrayList<>();

      for (int j = block.insts.size() - 1; j >= 0; j--) {
        var inst = block.insts.get(j);

        if (inst.def() != null && inst.def() instanceof VirtualRegister) {
          if (save.contains(inst.def())) {
            newInsts.add(0, inst);
            if (inst.use1() != null && inst.use1() instanceof VirtualRegister)
              save.add(inst.use1());
            if (inst.use2() != null && inst.use2() instanceof VirtualRegister)
              save.add(inst.use2());
          }
        } else {
          newInsts.add(0, inst);
          if (inst.use1() != null && inst.use1() instanceof VirtualRegister)
            save.add(inst.use1());
          if (inst.use2() != null && inst.use2() instanceof VirtualRegister)
            save.add(inst.use2());
        }
      }

      block.insts = newInsts;
    }
  }

}
