package Frontend;

import AST.*;
import AST.Expr.*;
import AST.Stmt.*;
import IR.*;
import IR.Inst.*;
import IR.Util.*;
import IR.Util.Entity.*;
import Util.Scope.*;
import Util.Type.*;
import Util.*;

import java.util.ArrayList;

public class IRBuilder implements ASTVisitor {

  IRProgram irProgram = null;
  IRBlock currentBlock = null;
  IRScope currentScope = null;
  GlobalScope globalScope = null;
  ExprVar lastExpr = null;

  public IRBuilder(IRProgram irProgram, GlobalScope globalScope) {
    this.irProgram = irProgram;
    this.globalScope = globalScope;
  }

  private IRType transType(ExprType type) {
    if (type.isFunc) throw new RuntimeException("IREntity: function type");
    if (type.isVoid) return new IRType("void");
    else if (type.dim > 0 || type.isNull || type.isClass || type.isString) return new IRType("ptr");
    else if (type.isInt) return new IRType("i32");
    else if (type.isBool) return new IRType("i1");
    else throw new RuntimeException("IREntity: unknown type");
  }

  private void exitScope() {
    currentScope.exit(currentBlock.parent.idCnt);
    currentScope = currentScope.parent;
  }

  public void visit(ProgramNode node) {
    for (var def : node.decls) {
      def.accept(this);
    }
  }

  public void visit(FuncDefNode node) {
    currentScope = new IRScope(currentScope);
    String tmp_name = node.name;
    if (currentScope.className != null)
      tmp_name = currentScope.className + "::" + tmp_name;
    var func = new IRFunction(transType(new ExprType(node.retType)), tmp_name);
    irProgram.functions.put(tmp_name, func);
    currentBlock = func.body.get("entry");

    node.paraList.accept(this);
    for (var stmt : node.stmt_List) {
      stmt.accept(this);
    }

//    currentScope.exit(func.idCnt); // 不需要：是退出整个函数，这个module中已经不会再添加新东西了
    currentScope = currentScope.parent;
    currentBlock = null;
  }
  public void visit(ClassDefNode node) {
    ArrayList<IRType> memberTypes = new ArrayList<>();
    for (var member : node.varDef_list) {
      for (int i = 0; i < member.varList.size(); ++i) {
        memberTypes.add(transType(new ExprType(member.type)));
      }
    }
    irProgram.structs.put(node.name, new StructDef(node.name, memberTypes));
    currentScope = new IRScope(currentScope, node.name);

    node.classBuild.accept(this);
    for (var func : node.funcDef_list) {
      func.accept(this);
    }

    currentScope = null;
  }
  public void visit(VarDefNode node) {
    IRType varType = transType(new ExprType(node.type));
    if (currentScope == null) { // in global scope
      // global variables don't need to rename
      for (var pair : node.varList) {
        GlobalPtr ptr = new GlobalPtr(pair.first);

        if (pair.second == null) { // no initial value
          irProgram.globals.put(pair.first, new GlobalVarDef(ptr, null, varType));
          continue;
        }
        if (pair.second instanceof AtomExprNode tmp && (tmp.isIntConst || tmp.isTrue || tmp.isFalse || tmp.isNull)) {
          irProgram.globals.put(pair.first, new GlobalVarDef(ptr, ((AtomExprNode) pair.second).identifier, varType));
          continue;
        }
        irProgram.globals.put(pair.first, new GlobalVarDef(ptr, null, varType));

        // do initialization
        if (!irProgram.functions.containsKey("_init")) irProgram.functions.put("_init", new IRFunction(new IRType("void"), "_init"));
        currentBlock = irProgram.functions.get("_init").body.get("entry");
        pair.second.accept(this);
        currentBlock.addInst(new Store(lastExpr.value, ptr));
      }
    } else { // in local scope
      for (var pair : node.varList) {
        currentScope.addVar(pair.first);
        int no = currentBlock.parent.idCnt.getOrDefault(pair.first, 0);
        currentBlock.parent.idCnt.put(pair.first, ++no);
        LocalPtr ptr = new LocalPtr(pair.first + "." + no);
        currentBlock.addInst(new Alloca(pair.first + "." + no, varType));
        if (pair.second != null) { // no initial value
          pair.second.accept(this);
          currentBlock.addInst(new Store(lastExpr.value, ptr));
        }
      }
    }
  }
  public void visit(ParaListNode node) {
    // already in entry block(currentBlock)
    var tmp = currentBlock.parent;
    if (currentScope.className != null) // if in class, add a "this" parameter
      tmp.paras.add(new Pair<>(new IRType("ptr"), "this"));
    for (var para : node.paras) {
      // i32 %a
      tmp.paras.add(new Pair<>(transType(new ExprType(para.first)), para.second));
    }

    for (var para : tmp.paras) {
      currentScope.addVar(para.second);
      int no = currentBlock.parent.idCnt.getOrDefault(para.second, 0);
      currentBlock.parent.idCnt.put(para.second, ++no);
      // add: %a.1 = alloca i32
      currentBlock.addInst(new Alloca(para.second + "." + no, para.first));
      // add: store i32 %a, ptr %a.1
      currentBlock.addInst(new Store(new LocalVar(para.first, para.second), new LocalPtr(para.second + "." + no)));
    }
  }
  public void visit(ClassBuildNode node) {
    var func = new IRFunction(new IRType("void"), currentScope.className + "::" + node.name);
    func.paras.add(new Pair<>(new IRType("ptr"), "this"));
    irProgram.functions.put(currentScope.className + "::" + node.name, func);
    currentScope = new IRScope(currentScope);
    currentBlock = func.body.get("entry");

    currentScope.addVar("this");
    func.idCnt.put("this", 1);
    currentBlock.addInst(new Alloca("this.1", new IRType("ptr")));
    currentBlock.addInst(new Store(new LocalVar(new IRType("ptr"), "this"), new LocalPtr("this.1")));

    for (var stmt : node.stmt_List) {
      stmt.accept(this);
    }

    currentScope = currentScope.parent;
    currentBlock = null;
  }

  public void visit(BlockStmtNode node) {
    currentScope = new IRScope(currentScope);
    for (var stmt : node.stmts) {
      stmt.accept(this);
    }
    exitScope();
  }
  public void visit(IfStmtNode node) {
    node.cond.accept(this);

    int no = currentBlock.parent.ifCnt++;
    if (node.elseStmt != null)
      currentBlock.addInst(new Br((LocalVar) lastExpr.value, "if.then." + no, "if.else." + no));
    else
      currentBlock.addInst(new Br((LocalVar) lastExpr.value, "if.then." + no, "if.end." + no));

    currentBlock = currentBlock.parent.addBlock("if.then." + no);
    currentScope = new IRScope(currentScope);
    node.thenStmt.accept(this);
    currentBlock.addInst(new Jump("if.end." + no));
    exitScope();

    if (node.elseStmt != null) {
      currentBlock = currentBlock.parent.addBlock("if.else." + no);
      currentScope = new IRScope(currentScope);
      node.elseStmt.accept(this);
      currentBlock.addInst(new Jump("if.end." + no));
      exitScope();
    }

    currentBlock = currentBlock.parent.addBlock("if.end." + no);
  }
  public void visit(WhileStmtNode node) {
    int no = currentBlock.parent.whileCnt++;
    currentBlock.addInst(new Jump("while.cond." + no));
    currentBlock = currentBlock.parent.addBlock("while.cond." + no);
    node.cond.accept(this);
    currentBlock.addInst(new Br((LocalVar) lastExpr.value, "while.body." + no, "while.end." + no));

    currentBlock = currentBlock.parent.addBlock("while.body." + no);
    currentScope = new IRScope(currentScope, 2, no);
    node.body.accept(this);
    currentBlock.addInst(new Jump("while.cond." + no));
    exitScope();

    currentBlock = currentBlock.parent.addBlock("while.end." + no);
  }
  public void visit(ForStmtNode node) {
    int no = currentBlock.parent.forCnt++;
    currentScope = new IRScope(currentScope, 1, no);
    if (node.initStmt != null)
      node.initStmt.accept(this);
    currentBlock.addInst(new Jump("for.cond." + no));

    currentBlock = currentBlock.parent.addBlock("for.cond." + no);
    if (node.condExpr != null) {
      node.condExpr.accept(this);
      currentBlock.addInst(new Br((LocalVar) lastExpr.value, "for.body." + no, "for.end." + no));
    }
    else {
      currentBlock.addInst(new Jump("for.body." + no));
    }

    currentBlock = currentBlock.parent.addBlock("for.body." + no);
    if (node.body != null)
      node.body.accept(this);
    currentBlock.addInst(new Jump("for.step." + no));

    currentBlock = currentBlock.parent.addBlock("for.step." + no);
    if (node.stepExpr != null)
      node.stepExpr.accept(this);
    currentBlock.addInst(new Jump("for.cond." + no));

    exitScope();
    currentBlock = currentBlock.parent.addBlock("for.end." + no);
  }
  public void visit(ContinueStmtNode node) {
    if (currentScope.loopType == 0) throw new RuntimeException("continue not in loop");
    if (currentScope.loopType == 1) {
      currentBlock.addInst(new Jump("for.step." + currentScope.loopNo));
    }
    else {
      currentBlock.addInst(new Jump("while.cond." + currentScope.loopNo));
    }
  }
  public void visit(BreakStmtNode node) {
    if (currentScope.loopType == 0) throw new RuntimeException("break not in loop");
    if (currentScope.loopType == 1) {
      currentBlock.addInst(new Jump("for.end." + currentScope.loopNo));
    }
    else {
      currentBlock.addInst(new Jump("while.end." + currentScope.loopNo));
    }
  }
  public void visit(ReturnStmtNode node) {
    if (node.retExpr == null) {
      currentBlock.addInst(new Ret(null));
    } else {
      node.retExpr.accept(this);
      currentBlock.addInst(new Ret(lastExpr.value));
    }
  }
  public void visit(ExprStmtNode node) {
    node.expr.accept(this);
  }
  public void visit(VardefStmtNode node) {
    node.varDef.accept(this);
  }

  public void visit(AtomExprNode node) {
    //todo
  }
  public void visit(BinaryExprNode node) {

    node.lhs.accept(this);
    var lhsVar = lastExpr.value;

    node.rhs.accept(this);
    var rhsVar = lastExpr.value;

    LocalVar retVar = new LocalVar(transType(node.type), String.valueOf(currentBlock.parent.varCnt++));
    if (node.op.equals("==") || node.op.equals("!=") || node.op.equals(">") || node.op.equals(">=") || node.op.equals("<") || node.op.equals("<=")) {
      currentBlock.addInst(new Icmp(retVar, node.op, lhsVar, rhsVar));
    } else {
      currentBlock.addInst(new Binary(retVar, node.op, lhsVar, rhsVar));
    }
    lastExpr = new ExprVar(retVar, null);
  }
  public void visit(UnaryExprNode node) { // all right value
    node.expr.accept(this);

    var tmp = new LocalVar(transType(node.type), String.valueOf(currentBlock.parent.varCnt++));
    switch (node.op) {
      case "!" -> {
        currentBlock.addInst(new Binary(tmp, "^", lastExpr.value, new IRLiteral("true", new IRType("i1"))));
        lastExpr.value = tmp;
      }
      case "~" -> {
        currentBlock.addInst(new Binary(tmp, "^", lastExpr.value, new IRLiteral("-1", new IRType("i32"))));
        lastExpr.value = tmp;
      }
      case "++", "--" -> {
        currentBlock.addInst(new Binary(tmp, node.op.equals("++") ? "+" : "-", lastExpr.value, new IRLiteral("1", new IRType("i32"))));
        currentBlock.addInst(new Store(tmp, lastExpr.destptr));
      }
      case "+", "-" -> {
        currentBlock.addInst(new Binary(tmp, node.op, new IRLiteral("0", new IRType("i32")), lastExpr.value));
        lastExpr.value = tmp;
      }
      default -> throw new RuntimeException("unknown unary operator");
    }
    lastExpr.destptr = null;
  }
  public void visit(PreSelfExprNode node) {
    node.expr.accept(this);
    var tmp = new LocalVar(transType(node.type), String.valueOf(currentBlock.parent.varCnt++));
    currentBlock.addInst(new Binary(tmp, node.op.equals("++") ? "+" : "-", lastExpr.value, new IRLiteral("1", new IRType("i32"))));
    currentBlock.addInst(new Store(tmp, lastExpr.destptr));
    lastExpr.value = tmp; // destptr 不变
  }
  public void visit(AssignExprNode node) {
    node.lhs.accept(this);
    var dst = lastExpr.destptr;
    node.rhs.accept(this);
    currentBlock.addInst(new Store(lastExpr.value, dst)); // lashExpr不变 为右值
  }
  public void visit(CallExprNode node){
    //todo
  }
  public void visit(ArrayExprNode node) {
    //todo
  }
  public void visit(MemberExprNode node) {
    //todo
  }
  public void visit(NewExprNode node) {
    //todo
  }
  public void visit(ConditionalExprNode node) {
    node.cond.accept(this);

    int no = currentBlock.parent.condCnt++;
    currentBlock.addInst(new Br((LocalVar) lastExpr.value, "cond.then." + no, "cond.else." + no));

    currentBlock = currentBlock.parent.addBlock("cond.then." + no);
    node.thenExpr.accept(this);
    currentBlock.addInst(new Jump("cond.end." + no));

    currentBlock = currentBlock.parent.addBlock("cond.else." + no);
    node.elseExpr.accept(this);
    currentBlock.addInst(new Jump("cond.end." + no));

    currentBlock = currentBlock.parent.addBlock("cond.end." + no);
  }
}
