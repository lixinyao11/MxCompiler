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
      currentBlock.insts.add(index, new ASMArithImmInst(currentBlock, "+", new Register("sp"), new Register("sp"), new Immediate(stackSize)));
      for (int i = funcDef.manager.calleeRegCnt - 1; i >= 0; --i) {
        currentBlock.insts.add(index, new ASMLwInst(currentBlock, new Register("t" + i), new MemAddr(new Immediate((funcDef.manager.stackCnt + i) * 4), new Register("sp"))));
      }
    }
    // ! 修改每一个用到的paraOffset，偏移量应为栈大小-第几个参数
    for (int i = 0; i < funcDef.paras.size() - 8; ++i) {
      var paraName = funcDef.paras.get(i + 8).getName();
      var offset = funcDef.manager.paraOffset.get(paraName);
      ((ASMLwInst) offset).setAddr(new MemAddr(new Immediate(stackSize + i * 4), new Register("sp")));
    }
  }
  public void visit(IRFuncDecl funcDecl) {}
  public void visit(IRGlobalVarDef globalVarDef) {
    program.globalVars.add(new ASMGlobalVar(globalVarDef.var.name, globalVarDef.init.getIntValue()));
  }
  public void visit(IRStringLiteralDef stringLiteralDef) {
    program.strings.add(new ASMStringLiteral(stringLiteralDef.ptr.name, stringLiteralDef.orgValue));
  }
  public void visit(IRStructDef structDef) {}
  public void visit(IRAllocaInst inst) {
    // ! localVar对应的栈空间里，存放了ptr指向的地址
    inst.parent.parent.manager.addVirtualReg(inst.result.getName());
    var reg1 = new Register("t0");
    currentBlock.addInst(new ASMArithImmInst(currentBlock, "+", reg1, new Register("sp"), new Immediate(inst.parent.parent.manager.stackCnt++ * 4)));
    currentBlock.addInst(new ASMSwInst(currentBlock, reg1, inst.parent.parent.manager.getAddr(inst.result.getName())));
  }
  public void visit(IRBinaryInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    var reg1 = new Register("t0");
    var reg2 = new Register("t1");
    var reg3 = new Register("t2");

    loadIREntity(inst.rhs1, reg1, inst.parent.parent.manager);
    loadIREntity(inst.rhs2, reg2, inst.parent.parent.manager);

    currentBlock.addInst(new ASMArithInst(currentBlock, inst.op, reg3, reg1, reg2));
    var addr = inst.parent.parent.manager.addVirtualReg(inst.result.getName());
    currentBlock.addInst(new ASMSwInst(currentBlock, reg3, addr));
    inst.parent.parent.manager.updateCalleeReg(3);
  }
  public void visit(IRBrInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    var addr = inst.parent.parent.manager.getAddr(inst.cond.getName());
    var reg = new Register("t0");
    currentBlock.addInst(new ASMLwInst(currentBlock, reg, addr));
    currentBlock.addInst(new ASMBranchInst(currentBlock, "==", reg, null, new Label(inst.parent.parent.name + "_" + inst.elseLabel)));
  }
  public void visit(IRCallInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    // 保存ra
    var raAddr = inst.parent.parent.manager.saveCallerReg();
    currentBlock.addInst(new ASMSwInst(currentBlock, new Register("ra"), raAddr));

    // 处理参数，如果有溢出参数移动sp
    for (int i = 0; i < min(inst.args.size(), 8); ++i)
      loadIREntity(inst.args.get(i), new Register("a" + i), inst.parent.parent.manager);

    int spOffset = 0;
    if (inst.args.size() > 8) {
      var tmpReg = new Register("t0");
      for (int i = 0; i < inst.args.size() - 8; ++i) {
        var arg = inst.args.get(i + 8);
        loadIREntity(arg, tmpReg, inst.parent.parent.manager);
        currentBlock.addInst(new ASMSwInst(currentBlock, tmpReg, new MemAddr(new Immediate(-(inst.args.size() - 8 - i) * 4), new Register("sp"))));
      }
      spOffset = (inst.args.size() - 8) * 4;
      currentBlock.addInst(new ASMArithImmInst(currentBlock, "+", new Register("sp"), new Register("sp"), new Immediate(-spOffset)));
    }

    // call
    currentBlock.addInst(new ASMCallInst(currentBlock, new Label(inst.funcName)));
    // 恢复sp
    if (spOffset > 0) {
      currentBlock.addInst(new ASMArithImmInst(currentBlock, "+", new Register("sp"), new Register("sp"), new Immediate(spOffset)));
    }
    // 恢复ra
    currentBlock.addInst(new ASMLwInst(currentBlock, new Register("ra"), raAddr));
    // 从a0中取出返回值
    if (inst.result != null) {
      currentBlock.addInst(new ASMSwInst(currentBlock, new Register("a0"), inst.parent.parent.manager.addVirtualReg(inst.result.getName())));
    }
  }
  public void visit(IRGetElementPtrInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));

    var reg1 = new Register("t0");
    loadIREntity(inst.ptr, reg1, inst.parent.parent.manager); // reg1中是ptr中存放的地址

    var reg2 = new Register("t1");
    var reg3 = new Register("t2");
    var index = inst.indexs.get(inst.indexs.size() - 1);
    loadIREntity(index, reg2, inst.parent.parent.manager);
    currentBlock.addInst(new ASMLiInst(currentBlock, reg3, new Immediate(4)));
    currentBlock.addInst(new ASMArithInst(currentBlock, "*", reg2, reg2, reg3));
    currentBlock.addInst(new ASMArithInst(currentBlock, "+", reg1, reg1, reg2));

    inst.parent.parent.manager.updateCalleeReg(3);
    currentBlock.addInst(new ASMSwInst(currentBlock, reg1, inst.parent.parent.manager.addVirtualReg(inst.result.getName())));
  }
  public void visit(IRIcmpInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));

    var reg1 = new Register("t0");
    var reg2 = new Register("t1");
    var reg3 = new Register("t2");

    // lw or li rhs1 and rhs2 to regs
    loadIREntity(inst.rhs1, reg1, inst.parent.parent.manager);
    loadIREntity(inst.rhs2, reg2, inst.parent.parent.manager);

    // sub reg1, reg2
    currentBlock.addInst(new ASMArithInst(currentBlock, "-", reg3, reg1, reg2));
    // set if __ 0
    switch (inst.op) {
      case ">", "==", "<", "!=" -> currentBlock.addInst(new ASMSetInst(currentBlock, inst.op, reg1, reg3));
      case ">=" -> {
        currentBlock.addInst(new ASMSetInst(currentBlock, ">", reg1, reg3));
        currentBlock.addInst(new ASMSetInst(currentBlock, "==", reg2, reg3));
        currentBlock.addInst(new ASMArithInst(currentBlock, "|", reg1, reg1, reg2));
      }
      case "<=" -> {
        currentBlock.addInst(new ASMSetInst(currentBlock, "<", reg1, reg3));
        currentBlock.addInst(new ASMSetInst(currentBlock, "==", reg2, reg3));
        currentBlock.addInst(new ASMArithInst(currentBlock, "|", reg1, reg1, reg2));
      }
      default -> throw new RuntimeException("IcmpInst: invalid op");
    }

    // sw reg1 to result
    currentBlock.addInst(new ASMSwInst(currentBlock, reg1, inst.parent.parent.manager.addVirtualReg(inst.result.getName())));
    inst.parent.parent.manager.updateCalleeReg(3);
  }
  public void visit(IRJumpInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    currentBlock.addInst(new ASMJInst(currentBlock, inst.parent.parent.name + "_" + inst.destLabel));
  }
  public void visit(IRLoadInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));

    var reg = new Register("t0");
    loadIREntity(inst.ptr, reg, inst.parent.parent.manager); // reg中是ptr中存放的地址
    // 从地址中取出值
    currentBlock.addInst(new ASMLwInst(currentBlock, reg, new MemAddr(new Immediate(0), reg)));
    // 再存入result
    currentBlock.addInst(new ASMSwInst(currentBlock, reg, inst.parent.parent.manager.addVirtualReg(inst.result.getName())));
    inst.parent.parent.manager.updateCalleeReg(1);
  }
  public void visit(IRRetInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));

    // ! 恢复sp和registers: 在visit fucnDef的最后遍历一遍，找到所有ret，在前面添加
    // 保存返回值到a0
    if (inst.value != null)
      loadIREntity(inst.value, new Register("a0"), inst.parent.parent.manager);
    inst.parent.parent.manager.retInsts.add(currentBlock.addInst(new ASMRetInst(currentBlock)));
  }
  public void visit(IRStoreInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));

    var reg1 = new Register("t0");
    var reg2 = new Register("t1");

    if (inst.src instanceof LocalVar) {
      var srcName = ((LocalVar)inst.src).getName();
      // maybe is parameter
      if (inst.parent.parent.manager.paraRegister.containsKey(srcName)) {
        currentBlock.addInst(new ASMMvInst(currentBlock, reg1, inst.parent.parent.manager.paraRegister.get(srcName)));
      } else if (inst.parent.parent.manager.paraOffset.containsKey(srcName)) {
        inst.parent.parent.manager.paraOffset.put(srcName, currentBlock.addInst(new ASMLwInst(currentBlock, reg1, null)));
      } else {
        currentBlock.addInst(new ASMLwInst(currentBlock, reg1, inst.parent.parent.manager.getAddr(((LocalVar)inst.src).getName())));
      }
    } else if (inst.src instanceof IRLiteral) {
      currentBlock.addInst(new ASMLiInst(currentBlock, reg1, new Immediate(((IRLiteral)inst.src).getIntValue())));
    } else if (inst.src instanceof GlobalPtr) {
      currentBlock.addInst(new ASMLaInst(currentBlock, reg1, ((GlobalPtr)inst.src).name));
    } else {
      throw new RuntimeException("StoreInst: invalid src");
    }

    loadIREntity(inst.pos, reg2, inst.parent.parent.manager); // reg2中是ptr中存放的地址
    // 把值store进地址中
    currentBlock.addInst(new ASMSwInst(currentBlock, reg1, new MemAddr(new Immediate(0), reg2)));
    inst.parent.parent.manager.updateCalleeReg(2);
  }

  // * it will load the content of entity into rd
  private void loadIREntity(IREntity entity, Register rd, FuncManager manager) {
    if (entity instanceof GlobalPtr) {
      currentBlock.addInst(new ASMLaInst(currentBlock, rd, ((GlobalPtr)entity).name));
//      currentBlock.addInst(new ASMLwInst(currentBlock, rd, new MemAddr(new Immediate(0), rd)));
    } else if (entity instanceof LocalVar) {
      currentBlock.addInst(new ASMLwInst(currentBlock, rd, manager.getAddr(((LocalVar)entity).getName())));
    } else if (entity instanceof IRLiteral) {
      currentBlock.addInst(new ASMLiInst(currentBlock, rd, new Immediate(((IRLiteral)entity).getIntValue())));
    } else {
      throw new RuntimeException("invalid entity");
    }
  }
}
