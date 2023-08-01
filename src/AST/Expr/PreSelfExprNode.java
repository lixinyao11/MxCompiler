package AST.Expr;

import Util.Position;
import AST.*;

public class PreSelfExprNode extends ExprNode {
  public String op = null;
  public ExprNode expr = null;

  public PreSelfExprNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
