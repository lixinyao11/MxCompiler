package AST;

import Util.Position;
import Util.Type.*;
import java.util.ArrayList;
import AST.Stmt.*;

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
