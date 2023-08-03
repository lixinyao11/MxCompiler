package AST.Expr;

import Util.Position;
import Util.Type.*;
import AST.*;

public class NewExprNode extends ExprNode {
  public VarType varType = null;
  public ExprNode expr = null; // for array: typename[expr][][]...

  public NewExprNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
