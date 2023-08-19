package ir.util;

import java.util.HashMap;

public class IRScope {
  public IRScope parent = null;
  HashMap<String, Integer> varNames = null;
  public String className = null; // null: not in class, else: in class
  public int loopType = 0; // not in loop: 0, in for: 1, in while: 2
  public int loopNo = 0;

  public IRScope(IRScope parent) {
    this.parent = parent;
    if (parent != null) {
      this.className = parent.className;
      this.loopType = parent.loopType;
      this.loopNo = parent.loopNo;
    }
    varNames = new HashMap<>();
  }

  public IRScope(IRScope parent, String className) {
    this.parent = parent;
    this.className = className;
    if (parent != null) {
      this.loopType = parent.loopType;
      this.loopNo = parent.loopNo;
    }
    varNames = new HashMap<>();
  }

  public IRScope(IRScope parent, int loopType, int loopNum) {
    this.parent = parent;
    if (parent != null) this.className = parent.className;
    this.loopType = loopType;
    this.loopNo = loopNum;
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
