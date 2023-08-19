package frontend;

import ast.*;
import ast.expr.*;
import ast.stmt.*;
import util.decl.*;
import util.scope.*;

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
    globalScope.addFuncSafe(node.name, new FuncDecl(node), node.pos);
    // ensure functions in globalScope are not redefined
  }

  public void visit(ClassDefNode node) {
    globalScope.addClassSafe(node.name, new ClassDecl(node), node.pos);
    // ensure class in globalScope are not redefined
    node.scope = new ClassScope(globalScope, node);
    // ensure variables and functions in class are not redefined repectively
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
