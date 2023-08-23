package ir.inst;

import ir.IRBlock;
import ir.IRVisitor;
import ir.util.entity.IREntity;
import ir.util.entity.LocalVar;

import java.util.ArrayList;

public class IRCallInst extends IRInst {
  public LocalVar result = null; // result and retType
  public String funcName = null;
  public ArrayList<IREntity> args = null; // may be literal, var, or reg

  public IRCallInst(IRBlock parent, LocalVar result, String funcName) {
    super(parent);
    this.result = result;
    this.funcName = funcName;
    this.args = new ArrayList<>();
  }

  public IRCallInst(IRBlock parent, LocalVar result, String funcName, IREntity arg1) {
    super(parent);
    this.result = result;
    this.funcName = funcName;
    this.args = new ArrayList<>();
    this.args.add(arg1);
  }

  public IRCallInst(IRBlock parent, LocalVar result, String funcName, IREntity arg1, IREntity arg2) {
    super(parent);
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

  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
