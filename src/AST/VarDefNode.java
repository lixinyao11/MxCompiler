package AST;

import AST.Expr.*;
import Util.Position;
import Util.Type.*;
import java.util.ArrayList;
import org.antlr.v4.runtime.misc.Pair;

public class VarDefNode extends ASTNode {
  public VarType type = null;
  public ArrayList<Pair<String, ExprNode>> varList = null;
  
  public VarDefNode(Position pos) {
    super(pos);
    varList = new ArrayList<>();
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
