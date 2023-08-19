package ast;

import util.Position;
import ast.type.*;
import java.util.ArrayList;
import ast.stmt.*;

public class FuncDefNode extends ASTNode {
  public ReturnType retType = null;
  public String name = null;
  public ParaListNode paraList = null;
  public ArrayList<StmtNode> stmt_List = null; // block

  public FuncDefNode(Position pos) {
    super(pos);
    stmt_List = new ArrayList<>();
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
