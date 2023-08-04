package AST.Stmt;

import AST.Expr.*;
import Util.Position;
import AST.*;

public class ForStmtNode extends StmtNode {
  public ExprNode condExpr = null, stepExpr = null;
  public StmtNode initStmt = null, body = null; // can be null (emptyStmt)

  public ForStmtNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
