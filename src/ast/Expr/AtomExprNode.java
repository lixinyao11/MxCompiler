package ast.expr;

import util.Position;
import ast.*;

public class AtomExprNode extends ExprNode {
  public boolean isThis = false, isTrue = false, isFalse = false;
  public boolean isIntConst = false, isStringConst = false, isNull = false, isIdentifier = false;
  public String identifier = null; // 若为string，不带\", \".

  public AtomExprNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
