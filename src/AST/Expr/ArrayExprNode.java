package AST.Expr;

import Util.Position;
import AST.*;

public class ArrayExprNode extends ExprNode {
  public ExprNode array = null, index = null;

  public ArrayExprNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
