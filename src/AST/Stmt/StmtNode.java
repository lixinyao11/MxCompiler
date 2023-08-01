package AST.Stmt;

import AST.*;
import Util.Position;

abstract public class StmtNode extends ASTNode {
  
  public StmtNode(Position pos) {
    super(pos);
  }

  @Override
  abstract public void accept(ASTVisitor visitor);
}
