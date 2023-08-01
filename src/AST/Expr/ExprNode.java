package AST.Expr;

import AST.ASTNode;
import Util.Position;
import AST.*;

abstract public class ExprNode extends ASTNode {
  
  public ExprNode(Position pos) {
    super(pos);
  }

  @Override
  abstract public void accept(ASTVisitor visitor);
}
