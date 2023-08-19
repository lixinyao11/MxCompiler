package ast.expr;

import ast.ASTNode;
import util.*;
import ast.*;
import ast.type.*;

abstract public class ExprNode extends ASTNode {
  public ExprType type = null;
  public boolean isLeftValue = false;
  
  public ExprNode(Position pos) {
    super(pos);
  }

  @Override
  abstract public void accept(ASTVisitor visitor);
}
