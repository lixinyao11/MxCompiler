package IR.Util;

import java.util.HashMap;
import java.util.HashSet;

public class IRScope {
  public IRScope parent = null;
  HashSet<String> var_names = null;
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
    var_names = new HashSet<>();
  }

  public IRScope(IRScope parent, String className) {
    this.parent = parent;
    this.className = className;
    if (parent != null) {
      this.loopType = parent.loopType;
      this.loopNo = parent.loopNo;
    }
    var_names = new HashSet<>();
  }

  public IRScope(IRScope parent, int loopType, int loopNum) {
    this.parent = parent;
    if (parent != null) this.className = parent.className;
    this.loopType = loopType;
    this.loopNo = loopNum;
    var_names = new HashSet<>();
  }

  public void addVar(String name) {
    var_names.add(name);
  }

  public void exit(HashMap<String, Integer> idCnt) {
    for (String name : var_names) {
      int cnt = idCnt.get(name);
      idCnt.replace(name, cnt - 1);
    }
  }
}
