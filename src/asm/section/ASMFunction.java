package asm.section;

import asm.inst.*;
import asm.operand.PhysicalRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ASMFunction {
  public ArrayList<ASMBlock> blocks = null;
  public String label = null;
  public int virtualCnt = 0;
  public int stackSize = 0;
  public ASMArithImmInst moveSpInst = null;
  public ArrayList<ASMArithImmInst> restoreSpInsts = new ArrayList<>();
  public HashMap<ASMCallInst, ASMSwInst> callerSaveRa = new HashMap<>();
  public HashMap<ASMCallInst, ASMLwInst> callerRestoreRa = new HashMap<>();
  public HashSet<PhysicalRegister> usedRegs = new HashSet<>();

  public ASMFunction(String label) {
    this.blocks = new ArrayList<>();
    this.label = label;
  }
  public ASMBlock addBlock(String label) {
    ASMBlock block = new ASMBlock(label, this);
    blocks.add(block);
    return block;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("      .section text\n").append("      .globl ").append(label).append("\n");
    for (ASMBlock block : blocks) {
      sb.append(block);
    }
    return sb.toString();
  }
}
