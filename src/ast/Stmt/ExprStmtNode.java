package ast.stmt;

import ast.expr.*;
import util.Position;
import ast.*;

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
