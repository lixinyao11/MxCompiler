package ast.stmt;

import ast.*;
import util.Position;

public class VardefStmtNode extends StmtNode {
  public VarDefNode varDef = null; 
 
  public VardefStmtNode(Position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
