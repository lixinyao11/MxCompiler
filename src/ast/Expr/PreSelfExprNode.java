package ast.expr;

import util.Position;
import ast.*;

public class PreSelfExprNode extends ExprNode { // ++a, --a
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
