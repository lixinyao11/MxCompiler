package middleend;

import ir.IRProgram;

public class IROptimize {
  IRProgram program;

  public IROptimize(IRProgram program) {
    this.program = program;
  }
  public void work() {
    new CFGBuilder(program).work();
//    new Mem2Reg(program).work();
  }
}
