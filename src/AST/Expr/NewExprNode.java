package AST.Expr;

import Util.Position;
import Util.Type.*;
import AST.*;
import java.util.ArrayList;

public class NewExprNode extends ExprNode {
  public VarType varType = null;
  public ArrayList<ExprNode> exprList = null; // for array: new typename[expr][expr][]...

  public NewExprNode(Position pos) {
    super(pos);
    exprList = new ArrayList<ExprNode>();
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
