package ast.expr;

import util.Position;
import ast.*;

public class ConditionalExprNode extends ExprNode {
  public ExprNode cond = null, thenExpr = null, elseExpr = null;

  public ConditionalExprNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
