package ast.expr;

import util.Position;
import ast.*;

public class BinaryExprNode extends ExprNode {
  public String op = null; // +-*/%  << >>  > >= < <=  == !=  & ^ | && ||
  public ExprNode lhs = null, rhs = null;

  public BinaryExprNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
