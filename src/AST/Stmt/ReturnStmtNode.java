package AST.Stmt;

import AST.Expr.*;
import Util.Position;
import AST.*;

public class ReturnStmtNode extends StmtNode {
  public ExprNode retExpr = null;

  public ReturnStmtNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
