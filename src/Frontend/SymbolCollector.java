package Frontend;

import AST.*;
import AST.Expr.*;
import AST.Stmt.*;
import Util.Decl.*;
import Util.*;
import Util.Scope.*;

public class SymbolCollector implements ASTVisitor {
  GlobalScope globalScope = null;

  public SymbolCollector(GlobalScope globalScope) {
    this.globalScope = globalScope;
  }

  public void visit(ProgramNode node) {
    for (var def : node.decls) {
      def.accept(this); // varDefNode: do nothing
    }
  }

  public void visit(FuncDefNode node) {
    if (globalScope.getFuncDecl(node.name) != null)
      throw new SemanticError("Function " + node.name + " has been defined", node.pos);
    globalScope.addFunc(node.name, new FuncDecl(node));
  }

  public void visit(ClassDefNode node) {
    if (globalScope.getClassDecl(node.name) != null)
      throw new SemanticError("Class " + node.name + " has been defined", node.pos);
    node.scope = new ClassScope(globalScope, node);
    globalScope.addClass(node.name, new ClassDecl(node));
  }
  public void visit(VarDefNode node) {}
  public void visit(ParaListNode node) {}

  public void visit(ClassBuildNode node) {}

  public void visit(BlockStmtNode node) {}
  public void visit(IfStmtNode node) {}
  public void visit(WhileStmtNode node) {}
  public void visit(ForStmtNode node) {}
  public void visit(ContinueStmtNode node) {}
  public void visit(BreakStmtNode node) {}
  public void visit(ReturnStmtNode node) {}
  public void visit(ExprStmtNode node) {}
  public void visit(VardefStmtNode node) {}

  public void visit(AtomExprNode node) {}
  public void visit(BinaryExprNode node) {}
  public void visit(UnaryExprNode node) {}
  public void visit(PreSelfExprNode node) {}
  public void visit(AssignExprNode node) {}
  public void visit(CallExprNode node) {}
  public void visit(ArrayExprNode node) {}
  public void visit(MemberExprNode node) {}
  public void visit(NewExprNode node) {}
  public void visit(ConditionalExprNode node) {}
  
}
