package backend;

import asm.ASMProgram;
import asm.inst.*;
import asm.operand.*;
import asm.section.ASMFunction;

import java.util.HashSet;

public class StackManager {
  ASMProgram program;

  public StackManager(ASMProgram program) {
    this.program = program;
  }
  public void work() {
    program.functions.forEach(this::workOnFunc);
  }

  private void callerSave(ASMFunction func) {
    for (var call : func.callerSaveRa.keySet()) {
      HashSet<Register> needSave = new HashSet<>();
      for (var reg : call.live) {
        assert reg instanceof PhysicalRegister;
        PhysicalRegister reg_ = (PhysicalRegister) reg;
        if (!reg_.isCalleeSave() && !reg_.name.equals("a0"))
          needSave.add(reg); // ! a0不用存
      }

      var saveInst = func.callerSaveRa.get(call);
      var restoreInst = func.callerRestoreRa.get(call);
      int saveIndex = saveInst.parent.insts.indexOf(saveInst);
      int restoreIndex = restoreInst.parent.insts.indexOf(restoreInst);
      for (var reg : needSave) {
        MemAddr addr = new MemAddr(new Immediate(func.stackSize), PhysicalRegister.get("sp"));
        func.stackSize += 4;
        saveInst.parent.insts.add(++saveIndex, new ASMSwInst(saveInst.parent, reg, addr));
        restoreInst.parent.insts.add(++restoreIndex, new ASMLwInst(restoreInst.parent, reg, addr));
      }
    }
  }
  private void calleeSave(ASMFunction func) {
    HashSet<Register> needSave = new HashSet<>();
    for (var reg : func.usedRegs) {
      if (reg.isCalleeSave() && !reg.name.equals("sp")) needSave.add(reg);
    }

    var saveInst = func.moveSpInst;
    int saveIndex = 0;
    for (var reg : needSave) {
      MemAddr addr = new MemAddr(new Immediate(func.stackSize), PhysicalRegister.get("sp"));
      func.stackSize += 4;
      saveInst.parent.insts.add(++saveIndex, new ASMSwInst(saveInst.parent, reg, addr));
      for (var restoreInst : func.restoreSpInsts) {
        int restoreIndex = restoreInst.parent.insts.indexOf(restoreInst);
        restoreInst.parent.insts.add(restoreIndex, new ASMLwInst(restoreInst.parent, reg, addr));
      }
    }
  }
  private void workOnFunc(ASMFunction func) {
    callerSave(func);
    calleeSave(func);
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
