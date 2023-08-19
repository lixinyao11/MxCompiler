package ast.stmt;

import util.Position;
import ast.*;

public class BreakStmtNode extends StmtNode {
  public BreakStmtNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
