package ast.stmt;

import util.Position;
import java.util.ArrayList;
import ast.*;

public class BlockStmtNode extends StmtNode {
  public ArrayList<StmtNode> stmts = null;
  
  public BlockStmtNode(Position pos) {
    super(pos);
    stmts = new ArrayList<>();
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
