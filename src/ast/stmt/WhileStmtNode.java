package ast.stmt;

import ast.expr.*;
import util.Position;
import ast.*;

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
