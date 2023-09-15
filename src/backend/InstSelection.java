package backend;

import asm.*;
import asm.inst.*;
import asm.operand.*;
import asm.section.*;
import ir.*;
import ir.inst.*;
import ir.module.*;
import ir.util.IRType;
import ir.util.entity.*;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.min;

public class InstSelection implements IRVisitor {
  ASMProgram program;
  ASMBlock currentBlock = null;
  HashMap<String, VirtualRegister> localVars = null;
  HashMap<String, ASMBlock> blocks = null;
  ArrayList<ASMRetInst> retInsts;
  int stackSize = 0;

  public InstSelection(ASMProgram program) {
    this.program = program;
  }

  public void visit(IRProgram program) {
    for (var funcDecl : program.funcDecls)
      funcDecl.accept(this);
    for (var structDef : program.structs.values())
      structDef.accept(this);
    for (var global : program.globals.values())
      global.accept(this);
    for (var stringLiteral : program.stringLiterals)
      stringLiteral.accept(this);
    for (var funcDef : program.functions.values())
      funcDef.accept(this);
  }
  public void visit(IRBlock block) {
    currentBlock = program.addBlock(block.parent.name + "_" + block.label);
    currentBlock.irBlock = block;
    block.asmBlock = currentBlock;
    blocks.put(block.label, currentBlock);
    for (var inst : block.instructions) {
      inst.accept(this);
    }
  }
  public void visit(IRFuncDef funcDef) {
    VirtualRegister.reset();
    this.localVars = new HashMap<>();
    this.retInsts = new ArrayList<>();
    this.blocks = new HashMap<>();
    var startBlock = currentBlock = program.addBlock(funcDef.name);

    for (int i = 0; i < funcDef.paras.size(); ++i) {
      VirtualRegister tmp = new VirtualRegister();
      if (i < 8)
        currentBlock.addInst(new ASMMvInst(currentBlock, tmp, new PhysicalRegister("a" + i)));
      else
        currentBlock.addInst(new ASMLwInst(currentBlock, tmp, new MemAddr(new Immediate(4 * (i - 8)), new PhysicalRegister("sp"))));
      localVars.put(funcDef.paras.get(i).getName(), tmp);
    }

    stackSize = 0;
    for (var block : funcDef.body) {
      block.accept(this); // 遇到LocalVar，去找找manager里的para或者virtual register
    }
    for (var block : funcDef.body) {
      block.phiInsts.forEach((key, phi) -> {
        phi.accept(this);
      });
    }

    currentBlock = startBlock;
    // 移动sp，提供本函数需要的栈空间，保存本函数需要的reg
    currentBlock.addInst(new ASMArithImmInst(currentBlock, "+", new PhysicalRegister("sp"), new PhysicalRegister("sp"), new Immediate(-stackSize)));
//    for (int i = 0; i < funcDef.manager.calleeRegCnt; ++i) {
//      currentBlock.addInst(new ASMSwInst(currentBlock, new Register("t" + i), new MemAddr(new Immediate((funcDef.manager.stackCnt + i) * 4), new Register("sp"))));
//    }
    // 所有ret前恢复sp，恢复reg
    for (var inst : retInsts) {
      currentBlock = inst.parent;
      int index = currentBlock.insts.indexOf(inst);
      currentBlock.insts.add(index, new ASMArithImmInst(currentBlock, "+", new PhysicalRegister("sp"), new PhysicalRegister("sp"), new Immediate(stackSize)));
//      for (int i = funcDef.manager.calleeRegCnt - 1; i >= 0; --i) {
//        currentBlock.insts.add(index, new ASMLwInst(currentBlock, new Register("t" + i), new MemAddr(new Immediate((funcDef.manager.stackCnt + i) * 4), new Register("sp"))));
//      }
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
    throw new RuntimeException("InstSelection.visit(IRAllocaInst)");
  }
  public void visit(IRBinaryInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    currentBlock.addInst(new ASMArithInst(currentBlock, inst.op, getIREntity(inst.result), getIREntity(inst.rhs1), getIREntity(inst.rhs2)));
  }
  public void visit(IRBrInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    currentBlock.addInst(new ASMBranchInst(currentBlock, "==", getIREntity(inst.cond), null, new Label(inst.parent.parent.name + "_" + inst.elseBlock.label)));
  }
  public void visit(IRCallInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));

    // save ra
    var raAddr = new MemAddr(new Immediate(stackSize), new PhysicalRegister("sp"));
    currentBlock.addInst(new ASMSwInst(currentBlock, new PhysicalRegister("ra"), raAddr));

    // ! 处理参数，如果有溢出参数移动sp
    for (int i = 0; i < min(inst.args.size(), 8); ++i)
      currentBlock.addInst(new ASMMvInst(currentBlock, new PhysicalRegister("a" + i), getIREntity(inst.args.get(i))));

    int spOffset = 0;
    if (inst.args.size() > 8) {
      for (int i = 0; i < inst.args.size() - 8; ++i)
        currentBlock.addInst(new ASMSwInst(currentBlock, getIREntity(inst.args.get(i + 8)), new MemAddr(new Immediate(-(inst.args.size() - 8 - i) * 4), new PhysicalRegister("sp"))));
      spOffset = (inst.args.size() - 8) * 4;
      currentBlock.addInst(new ASMArithImmInst(currentBlock, "+", new PhysicalRegister("sp"), new PhysicalRegister("sp"), new Immediate(-spOffset)));
    }

    // call
    currentBlock.addInst(new ASMCallInst(currentBlock, new Label(inst.funcName)));
    // ! 恢复sp
    if (spOffset > 0) {
      currentBlock.addInst(new ASMArithImmInst(currentBlock, "+", new PhysicalRegister("sp"), new PhysicalRegister("sp"), new Immediate(spOffset)));
    }
    // 恢复ra
    currentBlock.addInst(new ASMLwInst(currentBlock, new PhysicalRegister("ra"), raAddr));
    // 从a0中取出返回值
    if (inst.result != null)
      currentBlock.addInst(new ASMMvInst(currentBlock, getIREntity(inst.result), new PhysicalRegister("a0")));

    stackSize += 4;
  }
  public void visit(IRGetElementPtrInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    var ptr = getIREntity(inst.ptr);
    var index = getIREntity(inst.indexs.get(inst.indexs.size() - 1));
    currentBlock.addInst(new ASMArithInst(currentBlock, "*", index, index, getIREntity(new IRLiteral("4", new IRType("i32")))));
    currentBlock.addInst(new ASMArithInst(currentBlock, "+", ptr, ptr, index));

    currentBlock.addInst(new ASMMvInst(currentBlock, getIREntity(inst.result), ptr));
  }
  public void visit(IRIcmpInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));

    var reg1 = getIREntity(inst.rhs1);
    var reg2 = getIREntity(inst.rhs2);
    var reg3 = new VirtualRegister();

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

    // result = reg1
    currentBlock.addInst(new ASMMvInst(currentBlock, getIREntity(inst.result), reg1));
  }
  public void visit(IRJumpInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    currentBlock.addInst(new ASMJInst(currentBlock, inst.parent.parent.name + "_" + inst.destBlock.label));
  }
  public void visit(IRLoadInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    currentBlock.addInst(new ASMLwInst(currentBlock, getIREntity(inst.result), new MemAddr(new Immediate(0), getIREntity(inst.ptr))));
  }
  public void visit(IRStoreInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));
    currentBlock.addInst(new ASMSwInst(currentBlock, getIREntity(inst.src), new MemAddr(new Immediate(0), getIREntity(inst.dest))));
  }
  public void visit(IRRetInst inst) {
    currentBlock.addInst(new ASMComment(currentBlock, inst.toString()));

    // 保存返回值到a0
    if (inst.value != null)
      currentBlock.addInst(new ASMMvInst(currentBlock, new PhysicalRegister("a0"), getIREntity(inst.value)));
    retInsts.add((ASMRetInst) currentBlock.addInst(new ASMRetInst(currentBlock)));
  }
  public void visit(IRPhiInst inst) {
    Register result = getIREntity(inst.result);
    for (var block : inst.blocks) {
      currentBlock = blocks.get(block.label);
      currentBlock.addInst(new ASMMvInst(currentBlock, result, getIREntity(inst.values.get(inst.blocks.indexOf(block)))));
    }
  }

  private VirtualRegister getIREntity(IREntity entity) {
    if (entity instanceof GlobalPtr) {
      var tmp = new VirtualRegister();
      currentBlock.addInst(new ASMLaInst(currentBlock, tmp, ((GlobalPtr) entity).name));
      return tmp;
    } else if (entity instanceof IRLiteral) {
      var tmp = new VirtualRegister();
      currentBlock.addInst(new ASMLiInst(currentBlock, tmp, new Immediate(((IRLiteral) entity).getIntValue())));
      return tmp;
    } else if (entity instanceof LocalVar) {
      String name = ((LocalVar) entity).name;
      if (localVars.containsKey(name))
        return localVars.get(name);
      else {
        VirtualRegister ret = new VirtualRegister();
        localVars.put(name, ret);
        return ret;
      }
    } else {
      throw new RuntimeException("InstSelection.getIREntity");
    }
  }
}
