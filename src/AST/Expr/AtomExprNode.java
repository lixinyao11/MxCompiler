package AST.Expr;

import Util.Position;
import AST.*;

public class AtomExprNode extends ExprNode {
  public boolean isThis = false, isTrue = false, isFalse = false;
  public boolean isIntConst = false, isStringConst = false, isNull = false, isIdentifier = false;
  public String identifier = null;

  public AtomExprNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
