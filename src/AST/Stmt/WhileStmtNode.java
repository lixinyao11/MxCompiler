package AST.Stmt;

import AST.Expr.*;
import Util.Position;
import AST.*;

public class WhileStmtNode extends StmtNode {
  public ExprNode cond = null;
  public StmtNode body = null; // can be null(EmptyStmt)

  public WhileStmtNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
