package asm;

import asm.section.*;

import java.util.ArrayList;

public class ASMProgram {
  public ArrayList<ASMGlobalVar> globalVars = null;
  public ArrayList<ASMBlock> blocks = null;
  public ArrayList<ASMStringLiteral> strings = null;

  public ASMProgram() {
    this.blocks = new ArrayList<>();
    this.strings = new ArrayList<>();
    this.globalVars = new ArrayList<>();
  }

  public String toString() {
    StringBuilder ret = new StringBuilder();
    for (ASMBlock block : blocks)
      ret.append(block);
    for (ASMGlobalVar globalVar : globalVars)
      ret.append(globalVar);
    for (ASMStringLiteral string : strings)
      ret.append(string);
    return ret.toString();
  }

  public ASMBlock addBlock(String label) {
    ASMBlock block = new ASMBlock(label);
    blocks.add(block);
    return block;
  }
}
