package ast.stmt;

import ast.expr.*;
import util.Position;
import ast.*;

public class ReturnStmtNode extends StmtNode {
  public ExprNode retExpr = null;

  public ReturnStmtNode(Position pos) {
    super(pos);
  }

  public ReturnStmtNode(ExprNode retExpr, Position pos) {
    super(pos);
    this.retExpr = retExpr;
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
