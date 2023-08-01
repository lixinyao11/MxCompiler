package AST.Stmt;

import AST.*;
import Util.Position;

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
