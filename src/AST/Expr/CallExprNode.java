package AST.Expr;

import Util.Position;
import java.util.ArrayList;
import AST.*;

public class CallExprNode extends ExprNode {
  public ExprNode func = null;
  public ArrayList<ExprNode> args = null;

  public CallExprNode(Position pos) {
    super(pos);
    args = new ArrayList<>();
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
