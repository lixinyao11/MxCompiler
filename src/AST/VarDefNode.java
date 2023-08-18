package AST;

import AST.Expr.*;
import Util.*;
import AST.Type.*;
import java.util.ArrayList;

public class VarDefNode extends ASTNode {
  public VarType type = null;
  public ArrayList<Pair<String, ExprNode>> varList = null;
  
  public VarDefNode(Position pos) {
    super(pos);
    varList = new ArrayList<>();
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
