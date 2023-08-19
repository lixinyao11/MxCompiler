package ast.expr;

import util.Position;
import ast.*;

public class UnaryExprNode extends ExprNode {
  public String op = null; // ! | ~ | - | + | ++ | --
  public ExprNode expr = null;
  
  public UnaryExprNode(Position pos) {
    super(pos);
  }
  
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
