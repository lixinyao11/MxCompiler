package backend;

import asm.*;
import asm.inst.*;
import asm.operand.*;
import asm.section.*;
import ir.*;
import ir.inst.*;
import ir.module.*;
import ir.util.entity.*;

import java.util.HashMap;

import static java.lang.Math.min;

public class ASMBuilder implements IRVisitor {
  ASMProgram program = null;
  ASMBlock currentBlock = null;

  public ASMBuilder(ASMProgram program) {
    this.program = program;
  }
  public void visit(IRProgram program) {
    for (var funcDecl : program.funcDecls) {
      funcDecl.accept(this);
    }
    for (var structDef : program.structs.values()) {
      structDef.accept(this);
    }
    for (var global : program.globals.values()) {
      global.accept(this);
    }
    for (var stringLiteral : program.stringLiterals) {
      stringLiteral.accept(this);
    }
    for (var funcDef : program.functions.values()) {
      funcDef.accept(this);
    }
  }
  public void visit(IRBlock block) {
    currentBlock = program.addBlock(block.parent.name + "_" + block.label);
    for (var inst : block.instructions) {
      inst.accept(this);
    }
  }
  public void visit(IRFuncDef funcDef) {
    for (int i = 0; i < funcDef.paras.size(); ++i) {
      if (i < 8) {
        funcDef.manager.paraRegister.put(funcDef.paras.get(i).getName(), new Register("a" + i));
      } else {
        funcDef.manager.paraOffset.put(funcDef.paras.get(i).getName(), null);
        // ! 从下往上放
      }
    }
    var startBlock = currentBlock = program.addBlock(funcDef.name);
    for (var block : funcDef.body) {
      block.accept(this); // 遇到LocalVar，去找找manager里的para或者virtual register
    }

    int stackSize = funcDef.manager.getStackSize();
    // ! 在进入函数时移动sp，保存用到的regs
    currentBlock = startBlock;
    currentBlock.addInst(new ASMArithImmInst(currentBlock, "+", new Register("sp"), new Register("sp"), new Immediate(-stackSize)));
    for (int i = 0; i < funcDef.manager.calleeRegCnt; ++i) {
      currentBlock.addInst(new ASMSwInst(currentBlock, new Register("t" + i), new MemAddr(new Immediate((funcDef.manager.stackCnt + i) * 4), new Register("sp"))));
    }
    // ! 在退出函数时恢复sp，恢复用到的regs（每个ret前）
    for (var inst : funcDef.manager.retInsts) {
      currentBlock = inst.parent;
      int index = currentBlock.insts.indexOf(inst);
      for (int i = funcDef.manager.calleeRegCnt - 1; i >= 0; --i) {
        currentBlock.insts.add(index, new ASMLwInst(currentBlock, new Register("t" + i), new MemAddr(new Immediate((funcDef.manager.stackCnt + i) * 4), new Register("sp"))));
      }
      currentBlock.insts.add(index, new ASMArithImmInst(currentBlock, "+", new Register("sp"), new Register("sp"), new Immediate(stackSize)));
    }
    // ! 修改每一个用到的paraOffset，偏移量应为栈大小-第几个参数
    for (int i = 0; i < funcDef.paras.size() - 8; ++i) {
      var paraName = funcDef.paras.get(i + 8).getName();
      var offset = funcDef.manager.paraOffset.get(paraName);
      ((ASMLwInst) offset).setAddr(new MemAddr(new Immediate(stackSize - (funcDef.paras.size() - 8 - i) * 4), new Register("sp")));
    }
  }
  public void visit(IRFuncDecl funcDecl) {}
  public void visit(IRGlobalVarDef globalVarDef) {
    program.globalVars.add(new ASMGlobalVar(globalVarDef.var.name, globalVarDef.init.getIntValue()));
  }
  public void visit(IRStringLiteralDef stringLiteralDef) {
    program.strings.add(new ASMStringLiteral(stringLiteralDef.ptr.name, stringLiteralDef.value));
  }
  public void visit(IRStructDef structDef) {}
  public void visit(IRAllocaInst allocaInst) {
    allocaInst.parent.parent.manager.addVirtualReg(allocaInst.result.getName());
  }
  public void visit(IRBinaryInst binaryInst) {
    var reg1 = new Register("t0");
    var reg2 = new Register("t1");
    var reg3 = new Register("t2");

    visitIREntity(binaryInst.rhs1, reg1, binaryInst.parent.parent.manager);
    visitIREntity(binaryInst.rhs2, reg2, binaryInst.parent.parent.manager);

    currentBlock.addInst(new ASMArithInst(currentBlock, binaryInst.op, reg3, reg1, reg2));
    var addr = binaryInst.parent.parent.manager.addVirtualReg(binaryInst.result.getName());
    currentBlock.addInst(new ASMSwInst(currentBlock, reg3, addr));
    binaryInst.parent.parent.manager.updateCalleeReg(3);
  }
  public void visit(IRBrInst brInst) {
    var addr = brInst.parent.parent.manager.getAddr(brInst.cond.getName());
    var reg = new Register("t0");
    currentBlock.addInst(new ASMLwInst(currentBlock, reg, addr));
    currentBlock.addInst(new ASMBranchInst(currentBlock, "==", reg, null, new Label(brInst.elseLabel)));
  }
  public void visit(IRCallInst callInst) {
    // 保存ra
    var raAddr = callInst.parent.parent.manager.saveCallerReg();
    currentBlock.addInst(new ASMSwInst(currentBlock, new Register("ra"), raAddr));

    // 处理参数，如果有溢出参数移动sp
    for (int i = 0; i < min(callInst.args.size(), 8); ++i)
      visitIREntity(callInst.args.get(i), new Register("a" + i), callInst.parent.parent.manager);

    int spOffset = 0;
    if (callInst.args.size() > 8) {
      var tmpReg = new Register("t0");
      for (int i = 0; i < callInst.args.size() - 8; ++i) {
        var arg = callInst.args.get(i + 8);
        visitIREntity(arg, tmpReg, callInst.parent.parent.manager);
        currentBlock.addInst(new ASMSwInst(currentBlock, tmpReg, new MemAddr(new Immediate(-(callInst.args.size() - 8 - i) * 4), new Register("sp"))));
      }
      spOffset = (callInst.args.size() - 8) * 4;
      currentBlock.addInst(new ASMArithImmInst(currentBlock, "+", new Register("sp"), new Register("sp"), new Immediate(-spOffset)));
    }

    // call
    currentBlock.addInst(new ASMCallInst(currentBlock, new Label(callInst.funcName)));
    // 恢复ra
    currentBlock.addInst(new ASMLwInst(currentBlock, new Register("ra"), raAddr));
    // 恢复sp
    if (spOffset > 0) {
      currentBlock.addInst(new ASMArithImmInst(currentBlock, "+", new Register("sp"), new Register("sp"), new Immediate(spOffset)));
    }
    // 从a0中取出返回值
    if (callInst.result != null) {
      currentBlock.addInst(new ASMSwInst(currentBlock, new Register("a0"), callInst.parent.parent.manager.addVirtualReg(callInst.result.getName())));
    }
  }
  public void visit(IRGetElementPtrInst getElementPtrInst) {
    var reg1 = new Register("t0");
    visitIREntity(getElementPtrInst.ptr, reg1, getElementPtrInst.parent.parent.manager);

    var reg2 = new Register("t1");
    var reg3 = new Register("t2");
    for (var index : getElementPtrInst.indexs) {
      visitIREntity(index, reg2, getElementPtrInst.parent.parent.manager);
      currentBlock.addInst(new ASMLiInst(currentBlock, reg3, new Immediate(4)));
      currentBlock.addInst(new ASMArithInst(currentBlock, "*", reg2, reg2, reg3));
      currentBlock.addInst(new ASMArithInst(currentBlock, "+", reg1, reg1, reg2));
      currentBlock.addInst(new ASMLwInst(currentBlock, reg1, new MemAddr(new Immediate(0), reg1)));
    }
    getElementPtrInst.parent.parent.manager.updateCalleeReg(3);
    currentBlock.addInst(new ASMSwInst(currentBlock, reg1, getElementPtrInst.parent.parent.manager.addVirtualReg(getElementPtrInst.result.getName())));
  }
  public void visit(IRIcmpInst icmpInst) {
    var reg1 = new Register("t0");
    var reg2 = new Register("t1");
    var reg3 = new Register("t2");

    // lw or li rhs1 and rhs2 to regs
    visitIREntity(icmpInst.rhs1, reg1, icmpInst.parent.parent.manager);
    visitIREntity(icmpInst.rhs2, reg2, icmpInst.parent.parent.manager);

    // sub reg1, reg2
    currentBlock.addInst(new ASMArithInst(currentBlock, "-", reg3, reg1, reg2));
    // set if __ 0
    switch (icmpInst.op) {
      case ">", "==", "<", "!=" -> currentBlock.addInst(new ASMSetInst(currentBlock, icmpInst.op, reg1, reg3));
      case ">=" -> {
        currentBlock.addInst(new ASMSetInst(currentBlock, ">", reg1, reg3));
        currentBlock.addInst(new ASMSetInst(currentBlock, "==", reg2, reg3));
        currentBlock.addInst(new ASMArithInst(currentBlock, "or", reg1, reg1, reg2));
      }
      case "<=" -> {
        currentBlock.addInst(new ASMSetInst(currentBlock, "<", reg1, reg3));
        currentBlock.addInst(new ASMSetInst(currentBlock, "==", reg2, reg3));
        currentBlock.addInst(new ASMArithInst(currentBlock, "or", reg1, reg1, reg2));
      }
      default -> throw new RuntimeException("IcmpInst: invalid op");
    }

    // sw reg1 to result
    currentBlock.addInst(new ASMSwInst(currentBlock, reg1, icmpInst.parent.parent.manager.addVirtualReg(icmpInst.result.getName())));
    icmpInst.parent.parent.manager.updateCalleeReg(3);
  }
  public void visit(IRJumpInst jumpInst) {
    currentBlock.addInst(new ASMJInst(currentBlock, jumpInst.parent.parent.name + "_" + jumpInst.destLabel));
  }
  public void visit(IRLoadInst loadInst) {
    var reg = new Register("t0");
    visitIREntity(loadInst.ptr, reg, loadInst.parent.parent.manager);
    currentBlock.addInst(new ASMSwInst(currentBlock, reg, loadInst.parent.parent.manager.addVirtualReg(loadInst.result.getName())));
    loadInst.parent.parent.manager.updateCalleeReg(1);
  }
  public void visit(IRRetInst retInst) {
    // ! 恢复sp和registers: 在visit fucnDef的最后遍历一遍，找到所有ret，在前面添加
    // 保存返回值到a0
    if (retInst.value != null)
      visitIREntity(retInst.value, new Register("a0"), retInst.parent.parent.manager);
    retInst.parent.parent.manager.retInsts.add(currentBlock.addInst(new ASMRetInst(currentBlock)));
  }
  public void visit(IRStoreInst storeInst) {
    var reg1 = new Register("t0");
    var reg2 = new Register("t1");

    if (storeInst.src instanceof LocalVar) {
      var srcName = ((LocalVar)storeInst.src).getName();
      // maybe is parameter
      if (storeInst.parent.parent.manager.paraRegister.containsKey(srcName)) {
        currentBlock.addInst(new ASMMvInst(currentBlock, reg1, storeInst.parent.parent.manager.paraRegister.get(srcName)));
      } else if (storeInst.parent.parent.manager.paraOffset.containsKey(srcName)) {
        storeInst.parent.parent.manager.paraOffset.put(srcName, currentBlock.addInst(new ASMLwInst(currentBlock, reg1, null)));
      } else {
        currentBlock.addInst(new ASMLwInst(currentBlock, reg1, storeInst.parent.parent.manager.getAddr(((LocalVar)storeInst.src).getName())));
      }
    } else if (storeInst.src instanceof IRLiteral) {
      currentBlock.addInst(new ASMLiInst(currentBlock, reg1, new Immediate(((IRLiteral)storeInst.src).getIntValue())));
    } else {
      throw new RuntimeException("StoreInst: invalid src");
    }

    visitIREntity(storeInst.pos, reg2, storeInst.parent.parent.manager);

    currentBlock.addInst(new ASMSwInst(currentBlock, reg1, new MemAddr(new Immediate(0), reg2)));
    storeInst.parent.parent.manager.updateCalleeReg(2);
  }

  private void visitIREntity(IREntity entity, Register rd, FuncManager manager) {
    if (entity instanceof GlobalPtr) {
      currentBlock.addInst(new ASMLiInst(currentBlock, rd, new Immediate(((GlobalPtr)entity).name)));
    } else if (entity instanceof LocalVar) {
      currentBlock.addInst(new ASMLwInst(currentBlock, rd, manager.getAddr(((LocalVar)entity).getName())));
    } else if (entity instanceof IRLiteral) {
      currentBlock.addInst(new ASMLiInst(currentBlock, rd, new Immediate(((IRLiteral)entity).getIntValue())));
    } else {
      throw new RuntimeException("invalid entity");
    }
  }
}
