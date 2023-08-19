package ast;

import ast.expr.*;
import util.*;
import ast.type.*;
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
