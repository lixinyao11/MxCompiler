package ast.stmt;

import ast.expr.*;
import util.Position;
import ast.*;

public class IfStmtNode extends StmtNode {
  public ExprNode cond = null;
  public StmtNode thenStmt = null, elseStmt = null; // thenStmt can be null (emptyStmt)

  public IfStmtNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
