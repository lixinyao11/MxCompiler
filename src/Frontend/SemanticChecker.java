package Frontend;

import AST.*;
import AST.Expr.*;
import AST.Stmt.*;
import Util.Scope.*;
import Util.Decl.*;
import Util.*;

import java.util.ArrayList;

public class SemanticChecker implements ASTVisitor {
  GlobalScope globalScope;
  Scope currentScope;

  public SemanticChecker(GlobalScope globalScope) {
    this.globalScope = globalScope;
    this.currentScope = globalScope;
  }

  public void visit(ProgramNode node) {
    FuncDecl mainFunc = globalScope.getFuncDecl("main");
    if (mainFunc == null) {
      throw new SemanticError("No main function", node.pos);
    } else if (!mainFunc.returnsType(new Type("int", 0))) {
      throw new SemanticError("Main function should return int", node.pos);
    } else if (!mainFunc.checkParas(new ArrayList<Type>())) {
      throw new SemanticError("Main function should have no parameter", node.pos);
    }

    for (var def : node.decls) {
      def.accept(this);
    }
  }

  public void visit(FuncDefNode node);
  public void visit(ClassDefNode node);
  public void visit(VarDefNode node);
  public void visit(ParaListNode node);
  public void visit(ClassBuildNode node);

  public void visit(BlockStmtNode node);
  public void visit(IfStmtNode node);
  public void visit(WhileStmtNode node);
  public void visit(ForStmtNode node);
  public void visit(ContinueStmtNode node);
  public void visit(BreakStmtNode node);
  public void visit(ReturnStmtNode node);
  public void visit(ExprStmtNode node);
  public void visit(VardefStmtNode node);

  public void visit(AtomExprNode node);
  public void visit(BinaryExprNode node);
  public void visit(UnaryExprNode node);
  public void visit(PreSelfExprNode node);
  public void visit(AssignExprNode node);
  public void visit(CallExprNode node);
  public void visit(ArrayExprNode node);
  public void visit(MemberExprNode node);  
  public void visit(NewExprNode node);
  public void visit(ConditionalExprNode node);
  
}
