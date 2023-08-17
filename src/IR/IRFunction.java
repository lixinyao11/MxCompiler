package IR;

import IR.Util.Entity.LocalVar;
import IR.Util.IRType;
import java.util.ArrayList;
import java.util.HashMap;
import Util.Pair;

public class IRFunction {
  public IRType returnType = null;
  public String name = null; // ! name with "A::"A, but without "@"
  public ArrayList<LocalVar> paras = null; // ! string: name without "%"
  public ArrayList<IRBlock> body = null;
  public HashMap<String, Integer> idCnt = null;
  public int varCnt = 0, ifCnt = 0, forCnt = 0, whileCnt = 0, condCnt = 0;

  public IRFunction(IRType returnType, String name) {
    this.returnType = returnType;
    this.name = name;
    this.paras = new ArrayList<>();
    this.body = new ArrayList<>();
    this.idCnt = new HashMap<>();
    body.add(new IRBlock("entry", this));
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("define ").append(returnType.toString()).append(" @").append(name).append("(");
    for (int i = 0; i < paras.size(); ++i) {
      if (i > 0) sb.append(", ");
      sb.append(paras.get(i).getType().toString()).append(" ").append(paras.get(i).toString());
    }
    sb.append(") {\n");
    for (IRBlock block : body) {
      sb.append(block.toString());
    }
    sb.append("}\n");
    return sb.toString();
  }

  public IRBlock addBlock(String name) {
    IRBlock block = new IRBlock(name, this);
    body.add(block);
    return block;
  }

}
