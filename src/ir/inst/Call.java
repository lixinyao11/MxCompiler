package ir.inst;

import ir.util.entity.IREntity;
import ir.util.entity.LocalVar;

import java.util.ArrayList;

public class Call extends IRInst {
  public LocalVar result = null; // result and retType
  public String funcName = null;
  public ArrayList<IREntity> args = null; // may be literal, var, or reg

  public Call(LocalVar result, String funcName) {
    this.result = result;
    this.funcName = funcName;
    this.args = new ArrayList<>();
  }

  public Call(LocalVar result, String funcName, IREntity arg1) {
    this.result = result;
    this.funcName = funcName;
    this.args = new ArrayList<>();
    this.args.add(arg1);
  }

  public Call(LocalVar result, String funcName, IREntity arg1, IREntity arg2) {
    this.result = result;
    this.funcName = funcName;
    this.args = new ArrayList<>();
    this.args.add(arg1);
    this.args.add(arg2);
  }

  public String toString() {
    StringBuilder tmp = new StringBuilder();
    if (result == null)
      tmp.append("call void @").append(funcName).append("(");
    else
      tmp.append(result.toString()).append(" = call ").append(result.getType().toString()).append(" @").append(funcName).append("(");
    for (int i = 0; i < args.size(); ++i) {
      if (i != 0) tmp.append(", ");
      tmp.append(args.get(i).getType().toString()).append(" ").append(args.get(i).toString());
    }
    tmp.append(")");
    return tmp.toString();
  }
}
