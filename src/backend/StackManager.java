package backend;

import asm.ASMProgram;
import asm.inst.ASMArithInst;
import asm.inst.ASMLiInst;
import asm.operand.Immediate;
import asm.operand.PhysicalRegister;
import asm.section.ASMFunction;

public class StackManager {
  ASMProgram program;

  public StackManager(ASMProgram program) {
    this.program = program;
  }
  public void work() {
    program.functions.forEach(this::workOnFunc);
  }
  private void workOnFunc(ASMFunction func) {
    var t0 = PhysicalRegister.get("t0");
    var sp = PhysicalRegister.get("sp");

    if (func.stackSize < 1 << 11)
      func.moveSpInst.imm = new Immediate(-func.stackSize);
    else {
      var entryblock = func.moveSpInst.parent;
      int index = entryblock.insts.indexOf(func.moveSpInst);
      entryblock.insts.set(index, new ASMArithInst(entryblock, "+", sp, sp, t0));
      entryblock.insts.add(index, new ASMLiInst(entryblock, t0, new Immediate(-func.stackSize)));
    }

    for (var inst : func.restoreSpInsts) {
      if (func.stackSize < 1 << 11) {
        inst.imm = new Immediate(func.stackSize);
      } else {
        var exitblock = inst.parent;
        int index = exitblock.insts.indexOf(inst);
        exitblock.insts.set(index, new ASMArithInst(exitblock, "+", sp, sp, t0));
        exitblock.insts.add(index, new ASMLiInst(exitblock, t0, new Immediate(func.stackSize)));
      }
    }
  }
}
