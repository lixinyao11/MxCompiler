package AST.Stmt;

import AST.Expr.*;
import Util.Position;
import AST.*;

public class ForStmtNode extends StmtNode {
  public ExprNode init = null, cond = null, step = null;
  public StmtNode body = null;

  public ForStmtNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
