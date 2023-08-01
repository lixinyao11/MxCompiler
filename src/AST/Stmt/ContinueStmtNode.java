package AST.Stmt;

import Util.Position;
import AST.*;

public class ContinueStmtNode extends StmtNode {
  public ContinueStmtNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
