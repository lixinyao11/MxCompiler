package IR.Inst;

import IR.Util.Entity.IREntity;
import IR.Util.Entity.IRLiteral;
import IR.Util.Entity.IRVariable;
import IR.Util.Entity.LocalVar;
import IR.Util.IRType;

import java.util.ArrayList;

public class GetElementPtr extends IRInst {
  public LocalVar result = null; // must ptr type
  public String type; // %class.A  or  i32
  public IRVariable ptr = null; // must ptr type
  public ArrayList<IREntity> indexs = null;

  public GetElementPtr(LocalVar result, String type, IRVariable ptr) {
    this.result = result;
    this.type = type;
    this.ptr = ptr;
    this.indexs = new ArrayList<>();
  }

  public GetElementPtr(LocalVar result, String type, IRVariable ptr, IREntity index1) {
    this.result = result;
    this.type = type;
    this.ptr = ptr;
    this.indexs = new ArrayList<>();
    if (!(index1.getType().equals(new IRType("i32")))) throw new RuntimeException("index must be i32");
    this.indexs.add(index1);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(result.toString()).append(" = getelementptr ").append(type).append(", ptr ").append(ptr.toString());
    for (var index : indexs) {
      sb.append(", i32 ").append(index.toString());
    }
    return sb.toString();
  }

  public void addIndex(IREntity index) {
    if (!(index.getType().equals(new IRType("i32")))) throw new RuntimeException("index must be i32");
    indexs.add(index);
  }
}
