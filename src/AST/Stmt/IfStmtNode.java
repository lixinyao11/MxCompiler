package AST.Stmt;

import AST.Expr.*;
import Util.Position;
import AST.*;

public class IfStmtNode extends StmtNode {
  public ExprNode cond = null;
  public StmtNode thenStmt = null, elseStmt = null;

  public IfStmtNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
