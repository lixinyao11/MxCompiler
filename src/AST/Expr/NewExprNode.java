package AST.Expr;

import Util.Position;
import Util.Type;
import AST.*;

public class NewExprNode extends ExprNode {
  public Type type = null;
  public ExprNode expr = null;

  public NewExprNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
