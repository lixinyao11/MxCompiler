package ir;

import ir.inst.*;
import ir.module.*;

public interface IRVisitor {
  public void visit(IRProgram program);
  public void visit(IRBlock block);
  public void visit(IRFuncDef funcDef);
  public void visit(IRFuncDecl funcDecl);
  public void visit(IRGlobalVarDef globalVarDef);
  public void visit(IRStringLiteralDef stringLiteralDef);
  public void visit(IRStructDef structDef);
  public void visit(IRAllocaInst allocaInst);
  public void visit(IRBinaryInst binaryInst);
  public void visit(IRBrInst brInst);
  public void visit(IRCallInst callInst);
  public void visit(IRGetElementPtrInst getElementPtrInst);
  public void visit(IRIcmpInst icmpInst);
  public void visit(IRJumpInst jumpInst);
  public void visit(IRLoadInst loadInst);
  public void visit(IRRetInst retInst);
  public void visit(IRStoreInst storeInst);
}
