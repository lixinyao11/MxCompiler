package ast.stmt;

import ast.*;
import util.Position;

abstract public class StmtNode extends ASTNode {
  
  public StmtNode(Position pos) {
    super(pos);
  }

  @Override
  abstract public void accept(ASTVisitor visitor);
}
