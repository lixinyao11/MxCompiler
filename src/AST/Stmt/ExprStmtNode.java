package AST.Stmt;

import AST.Expr.*;
import Util.Position;
import AST.*;

public class ExprStmtNode extends StmtNode {
  public ExprNode expr = null;

  public ExprStmtNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
