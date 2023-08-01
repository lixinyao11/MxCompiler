package AST.Expr;

import Util.Position;
import AST.*;

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
