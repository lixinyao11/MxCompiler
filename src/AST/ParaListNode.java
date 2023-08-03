package AST;

import java.util.ArrayList;

import Util.Position;
import Util.Type.*;
import org.antlr.v4.runtime.misc.Pair;

public class ParaListNode extends ASTNode {
  public ArrayList<Pair<VarType, String>> paras = null;
  
  public ParaListNode(Position pos) {
    super(pos);
    paras = new ArrayList<>();
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
