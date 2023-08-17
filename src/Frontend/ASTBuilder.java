package Frontend;

import AST.*;
import AST.Stmt.*;
import AST.Expr.*;
import Parser.MxBaseVisitor;
import Parser.MxParser;
import Util.*;
import Util.Position;
import Util.Type.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {

  @Override
  public ASTNode visitTerminal(org.antlr.v4.runtime.tree.TerminalNode node) {
    throw new RuntimeException("Terminal Node should not be visited");
  }
  @Override
  public ASTNode visitProgram(MxParser.ProgramContext ctx) {
    ProgramNode program = new ProgramNode(new Position(ctx));
    for (var x : ctx.children)
      if (!(x instanceof TerminalNode)) program.decls.add(visit(x));
    return program;
  }

  @Override
  public ASTNode visitClassDef(MxParser.ClassDefContext ctx) {
    ClassDefNode classDef = new ClassDefNode(new Position(ctx));
    classDef.name = ctx.Identifier().getText();
    if (ctx.classBuild().size() > 1)
      throw new SemanticError("class can only have one constructor", new Position(ctx));
    if (!ctx.classBuild().isEmpty()) {
      classDef.classBuild = (ClassBuildNode) visitClassBuild(ctx.classBuild().get(0));
    } else {
      classDef.classBuild = null;
    }
    for (var x : ctx.varDef())
      classDef.varDef_list.add((VarDefNode) visitVarDef(x));
    for (var x : ctx.funDef())
      classDef.funcDef_list.add((FuncDefNode) visitFunDef(x));
    return classDef;
  }

  @Override
  public ASTNode visitClassBuild(MxParser.ClassBuildContext ctx) {
    ClassBuildNode classBuild = new ClassBuildNode(new Position(ctx));
    classBuild.name = ctx.Identifier().getText();
    for (var x : ctx.block().statement()) {
      if (x instanceof MxParser.EmptyStmtContext) continue;
      classBuild.stmt_List.add((StmtNode) visit(x));
    }
    return classBuild;
  }

  @Override
  public ASTNode visitFunDef(MxParser.FunDefContext ctx) {
    FuncDefNode func = new FuncDefNode(new Position(ctx));
    func.name = ctx.Identifier().getText();

    if (ctx.returnType().Void() != null) {
      func.retType = new ReturnType("void", 0);
    } else {
      func.retType = new ReturnType(ctx.returnType().type().typeName().getText(), ctx.returnType().type().LBracket().size());
    }

    if (ctx.paraList() != null) {
      func.paraList = (ParaListNode) visitParaList(ctx.paraList());
    } else {
      func.paraList = new ParaListNode(new Position(ctx));
    }

    for (var x : ctx.block().statement()) {
      if (x instanceof MxParser.EmptyStmtContext) continue;
      func.stmt_List.add((StmtNode) visit(x));
    }

    return func;
  }

  @Override
  public ASTNode visitParaList(MxParser.ParaListContext ctx) {
    ParaListNode paraList = new ParaListNode(new Position(ctx));
    for (int i = 0; i < ctx.type().size(); ++i) {
      VarType type = new VarType(ctx.type(i).typeName().getText(), ctx.type(i).LBracket().size());
      String name = ctx.Identifier(i).getText();
      paraList.paras.add(new Pair<>(type, name));
    }
    return paraList;
  }

  @Override
  public ASTNode visitBlockStmt(MxParser.BlockStmtContext ctx) {
    BlockStmtNode blockStmt = new BlockStmtNode(new Position(ctx));
    for (var x : ctx.block().statement()) {
      if (x instanceof MxParser.EmptyStmtContext) continue;
      blockStmt.stmts.add((StmtNode) visit(x));
    }
    return blockStmt;
  }

  @Override
  public ASTNode visitVardefStmt(MxParser.VardefStmtContext ctx) {
    VardefStmtNode vardefStmt = new VardefStmtNode(new Position(ctx));
    vardefStmt.varDef = (VarDefNode) visitVarDef(ctx.varDef());
    return vardefStmt;
  }

  @Override
  public ASTNode visitIfStmt(MxParser.IfStmtContext ctx) {
    IfStmtNode ifStmt = new IfStmtNode(new Position(ctx));
    ifStmt.cond = (ExprNode) visit(ctx.expression());
    ifStmt.thenStmt = (StmtNode) visit(ctx.thenStmt);
    if (ctx.elseStmt != null)
      ifStmt.elseStmt = (StmtNode) visit(ctx.elseStmt);
    return ifStmt;
  }

  @Override
  public ASTNode visitWhileStmt(MxParser.WhileStmtContext ctx) {
    WhileStmtNode whileStmt = new WhileStmtNode(new Position(ctx));
    whileStmt.cond = (ExprNode) visit(ctx.expression());
    whileStmt.body = (StmtNode) visit(ctx.statement());
    return whileStmt;
  }

  @Override
  public ASTNode visitForStmt(MxParser.ForStmtContext ctx) {
    ForStmtNode forStmt = new ForStmtNode(new Position(ctx));
    if (ctx.initStmt != null)
      forStmt.initStmt = (StmtNode) visit(ctx.initStmt);
    if (ctx.condExpr != null)
      forStmt.condExpr = (ExprNode) visit(ctx.condExpr);
    if (ctx.stepExpr != null)
      forStmt.stepExpr = (ExprNode) visit(ctx.stepExpr);
    forStmt.body = (StmtNode) visit(ctx.statement(1));
    return forStmt;
  }

  @Override
  public ASTNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
    ReturnStmtNode returnStmt = new ReturnStmtNode(new Position(ctx));
    if (ctx.expression() != null)
      returnStmt.retExpr = (ExprNode) visit(ctx.expression());
    return returnStmt;
  }

  @Override
  public ASTNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
    return new BreakStmtNode(new Position(ctx));
  }

  @Override
  public ASTNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
    return new ContinueStmtNode(new Position(ctx));
  }

  @Override
  public ASTNode visitExprStmt(MxParser.ExprStmtContext ctx) {
    ExprStmtNode exprStmt = new ExprStmtNode(new Position(ctx));
    exprStmt.expr = (ExprNode) visit(ctx.expression());
    return exprStmt;
  }

  @Override
  public ASTNode visitEmptyStmt(MxParser.EmptyStmtContext ctx) {
    return null;
  }

  @Override
  public ASTNode visitVarDef(MxParser.VarDefContext ctx) {
    VarDefNode varDef = new VarDefNode(new Position(ctx));
    varDef.type = new VarType(ctx.type().typeName().getText(), ctx.type().LBracket().size());
    for (int i = 0; i < ctx.Identifier().size(); ++i) {
      varDef.varList.add(new Pair<>(ctx.Identifier(i).getText(), ctx.expression(i) == null ? null : (ExprNode) visit(ctx.expression(i))));
    }
    return varDef;
  }

  @Override
  public ASTNode visitNewVarExpr(MxParser.NewVarExprContext ctx) {
    NewExprNode newExpr = new NewExprNode(new Position(ctx));
    newExpr.varType = new VarType(ctx.typeName().getText(), 0);
    return newExpr;
  }

  @Override
  public ASTNode visitUnaryExpr(MxParser.UnaryExprContext ctx) {
    UnaryExprNode unaryExpr = new UnaryExprNode(new Position(ctx));
    unaryExpr.op = ctx.op.getText();
    unaryExpr.expr = (ExprNode) visit(ctx.expression());
    return unaryExpr;
  }

  @Override
  public ASTNode visitArrayExpr(MxParser.ArrayExprContext ctx) {
    ArrayExprNode arrayExpr = new ArrayExprNode(new Position(ctx));
    arrayExpr.array = (ExprNode) visit(ctx.expression(0));
    arrayExpr.index = (ExprNode) visit(ctx.expression(1));
    return arrayExpr;
  }

  @Override
  public ASTNode visitPreSelfExpr(MxParser.PreSelfExprContext ctx) {
    PreSelfExprNode preSelfExpr = new PreSelfExprNode(new Position(ctx));
    preSelfExpr.op = ctx.op.getText();
    preSelfExpr.expr = (ExprNode) visit(ctx.expression());
    return preSelfExpr;
  }

  @Override
  public ASTNode visitMemberExpr(MxParser.MemberExprContext ctx) {
    MemberExprNode memberExpr = new MemberExprNode(new Position(ctx));
    memberExpr.obj = (ExprNode) visit(ctx.expression());
    memberExpr.member = ctx.Identifier().getText();
    return memberExpr;
  }

  @Override
  public ASTNode visitAtomExpr(MxParser.AtomExprContext ctx) {
    AtomExprNode atomExpr = new AtomExprNode(new Position(ctx));
    atomExpr.identifier = ctx.primary().getText();
    if (ctx.primary().This() != null) {
      atomExpr.isThis = true;
    } else if (ctx.primary().Identifier() != null) {
      atomExpr.isIdentifier = true;
    } else {
      if (ctx.primary().literal().True() != null) {
        atomExpr.isTrue = true;
      } else if (ctx.primary().literal().False() != null) {
        atomExpr.isFalse = true;
      } else if (ctx.primary().literal().Null() != null) {
        atomExpr.isNull = true;
      } else if (ctx.primary().literal().IntegerConst() != null) {
        atomExpr.isIntConst = true;
      } else if (ctx.primary().literal().StringConst() != null) {
        atomExpr.isStringConst = true;
        atomExpr.identifier = atomExpr.identifier.substring(1, atomExpr.identifier.length() - 1);
      } else {
        throw new RuntimeException("AtomExpr Error");
      }
    }
    return atomExpr;
  }

  @Override
  public ASTNode visitBinaryExpr(MxParser.BinaryExprContext ctx) {
    BinaryExprNode binaryExpr = new BinaryExprNode(new Position(ctx));
    binaryExpr.op = ctx.op.getText();
    binaryExpr.lhs = (ExprNode) visit(ctx.expression(0));
    binaryExpr.rhs = (ExprNode) visit(ctx.expression(1));
    return binaryExpr;
  }

  @Override
  public ASTNode visitCallExpr(MxParser.CallExprContext ctx) {
    CallExprNode callExpr = new CallExprNode(new Position(ctx));
    callExpr.func = (ExprNode) visit(ctx.expression(0));
    for (int i = 1; i < ctx.expression().size(); ++i) {
      callExpr.args.add((ExprNode) visit(ctx.expression(i)));
    }
    return callExpr;
  }

  @Override
  public ASTNode visitAssignExpr(MxParser.AssignExprContext ctx) {
    AssignExprNode assignExpr = new AssignExprNode(new Position(ctx));
    assignExpr.lhs = (ExprNode) visit(ctx.expression(0));
    assignExpr.rhs = (ExprNode) visit(ctx.expression(1));
    return assignExpr;
  }

  @Override
  public ASTNode visitNewArrayExpr(MxParser.NewArrayExprContext ctx) {
    NewExprNode newExpr = new NewExprNode(new Position(ctx));
    newExpr.varType = new VarType(ctx.typeName().getText(), ctx.LBracket().size());

    boolean flag = false;
    for (int i = 0; i < ctx.children.size(); ++i) {
      if (ctx.children.get(i).getText().equals("[")) {
        if (flag) {
          if (ctx.children.get(++i) instanceof MxParser.ExpressionContext) throw new SemanticError("NewArrayExpr Error", new Position(ctx));
        } else {
          if (ctx.children.get(++i) instanceof MxParser.ExpressionContext) {
            newExpr.exprList.add((ExprNode) visit(ctx.children.get(i)));
          } else {
            flag = true;
          }
        }
      }
    }

    return newExpr;
  }

  @Override
  public ASTNode visitParenExpr(MxParser.ParenExprContext ctx) {
    return visit(ctx.expression());
  }

  @Override
  public ASTNode visitConditionalExpr(MxParser.ConditionalExprContext ctx) {
    ConditionalExprNode conditionalExpr = new ConditionalExprNode(new Position(ctx));
    conditionalExpr.cond = (ExprNode) visit(ctx.expression(0));
    conditionalExpr.thenExpr = (ExprNode) visit(ctx.expression(1));
    conditionalExpr.elseExpr = (ExprNode) visit(ctx.expression(2));
    return conditionalExpr;
  }
}