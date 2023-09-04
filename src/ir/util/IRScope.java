package ir.util;

import ir.IRBlock;

import java.util.HashMap;

public class IRScope {
  public IRScope parent = null;
  HashMap<String, Integer> varNames = null;
  public String className = null; // null: not in class, else: in class
  public IRBlock loopEnd = null; // null: not in loop
  public IRBlock loopNext = null; // null: not in loop, for.step, while.cond

  public IRScope(IRScope parent) {
    this.parent = parent;
    if (parent != null) {
      this.className = parent.className;
      this.loopEnd = parent.loopEnd;
      this.loopNext = parent.loopNext;
    }
    varNames = new HashMap<>();
  }

  public IRScope(IRScope parent, String className) {
    this.parent = parent;
    this.className = className;
    if (parent != null) {
      this.loopEnd = parent.loopEnd;
      this.loopNext = parent.loopNext;
    }
    varNames = new HashMap<>();
  }

  public IRScope(IRScope parent, IRBlock loopEnd, IRBlock loopNext) {
    this.parent = parent;
    if (parent != null) this.className = parent.className;
    this.loopEnd = loopEnd;
    this.loopNext = loopNext;
    varNames = new HashMap<>();
  }

  public void addVar(String name, int no) {
    varNames.put(name, no);
  }

  public int getVarNo(String name) {
    if (varNames.containsKey(name)) return varNames.get(name);
    else if (parent != null) return parent.getVarNo(name);
    else return -1;
  }
}
