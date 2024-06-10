package middleend;

import java.io.IOException;

import ir.IRProgram;

public class IROptimize {
  IRProgram program;

  public IROptimize(IRProgram program) {
    this.program = program;
  }
  public void work() throws IOException {
    new CFGBuilder(program).work();
    new Mem2Reg(program).work();
    // System.out.write(program.toString().getBytes());
    new SCCP(program).work();
    new Inliner(program).work();
  }
}