package ir.inst;

import ir.IRBlock;
import ir.IRVisitor;
import ir.util.IRType;
import ir.util.entity.*;

import java.util.ArrayList;

public class IRPhiInst extends IRInst {
  public LocalVar result = null;
  IRType type = null;
  public ArrayList<IREntity> values = null;
  public ArrayList<IRBlock> blocks = null;

  public IRPhiInst(IRBlock parent, LocalVar result, IRType type) {
    super(parent);
    this.result = result;
    this.type = type;
    this.values = new ArrayList<>();
    this.blocks = new ArrayList<>();
  }

  public void addBranch(IREntity value, IRBlock block) {
    values.add(value);
    blocks.add(block);
  }

  public void changeBranch(IREntity value, IRBlock block) {
    for (var tmp : blocks) {
      if (tmp == block) {
        values.set(blocks.indexOf(tmp), value);
        return;
      }
    }
  }

  public void changeBlock(IRBlock oldBlock, IRBlock newBlock) {
    for (var tmp : blocks) {
      if (tmp == oldBlock) {
        blocks.set(blocks.indexOf(tmp), newBlock);
        return;
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(result.toString()).append(" = phi ").append(type.toString()).append(" ");
    for (int i = 0; i < values.size(); ++i) {
      sb.append("[ ").append(values.get(i).toString()).append(", %").append(blocks.get(i).label).append(" ]");
      if (i != values.size() - 1) sb.append(", ");
    }
    return sb.toString();
  }

  @Override
  public void accept(IRVisitor visitor) {
    visitor.visit(this);
  }
}
