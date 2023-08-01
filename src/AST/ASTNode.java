package AST;

import Util.Position;

abstract public class ASTNode {
  public Position pos = null;

  public ASTNode(Position pos) {
    this.pos = pos;
  }

  public abstract void accept(ASTVisitor visitor);
}