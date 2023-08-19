package ast;

import java.util.ArrayList;

import util.*;
import ast.type.*;

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
