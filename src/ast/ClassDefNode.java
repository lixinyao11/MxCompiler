package ast;

import java.util.ArrayList;
import util.*;
import util.scope.*;

public class ClassDefNode extends ASTNode {
  public String name = null;
  public ClassBuildNode classBuild = null;
  public ArrayList<VarDefNode> varDef_list = null;
  public ArrayList<FuncDefNode> funcDef_list = null;
  public ClassScope scope = null;

  public ClassDefNode(Position pos) {
    super(pos);
    varDef_list = new ArrayList<>();
    funcDef_list = new ArrayList<>();
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
