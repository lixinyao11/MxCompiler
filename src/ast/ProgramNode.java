package ast;

import util.Position;
import java.util.ArrayList;

public class ProgramNode extends ASTNode {
  public ArrayList<ASTNode> decls = null; // VarDefNode | ClassDefNode | FuncDefNode

  public ProgramNode(Position pos) {
    super(pos);
    decls = new ArrayList<>();
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
