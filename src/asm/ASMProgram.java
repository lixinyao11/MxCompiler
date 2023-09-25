package asm;

import asm.section.*;

import java.util.ArrayList;

public class ASMProgram {
  public ArrayList<ASMGlobalVar> globalVars = null;
  public ArrayList<ASMFunction> functions = null;
  public ArrayList<ASMStringLiteral> strings = null;

  public ASMProgram() {
    this.functions = new ArrayList<>();
    this.strings = new ArrayList<>();
    this.globalVars = new ArrayList<>();
  }

  public String toString() {
    StringBuilder ret = new StringBuilder();
    for (ASMFunction func : functions)
      ret.append(func);
    for (ASMGlobalVar globalVar : globalVars)
      ret.append(globalVar);
    for (ASMStringLiteral string : strings)
      ret.append(string);
    return ret.toString();
  }

  public ASMBlock addFunction(String label) {
    ASMFunction func = new ASMFunction(label);
    ASMBlock block = new ASMBlock(label, func);
    func.blocks.add(block);
    functions.add(func);
    return block;
  }
}
