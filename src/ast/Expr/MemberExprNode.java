package ast.expr;

import util.Position;
import ast.*;

public class MemberExprNode extends ExprNode {
  public ExprNode obj = null;
  public String member = null;

  public MemberExprNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
