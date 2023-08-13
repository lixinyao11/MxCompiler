package AST;

import java.util.ArrayList;

import Util.*;
import Util.Type.*;

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
