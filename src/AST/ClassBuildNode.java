package AST;

import Util.Position;
import java.util.ArrayList;

import AST.Stmt.StmtNode;

public class ClassBuildNode extends ASTNode {
  public String name = null;
  public ArrayList<StmtNode> stmt_List = null; // block

  public ClassBuildNode(Position pos) {
    super(pos);
    stmt_List = new ArrayList<>();
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
