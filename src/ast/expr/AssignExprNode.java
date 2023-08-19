package ast.expr;

import util.Position;
import ast.*;

public class AssignExprNode extends ExprNode {
  public ExprNode lhs = null, rhs = null;

  public AssignExprNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
