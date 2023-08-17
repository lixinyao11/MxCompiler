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
    var tmp = irProgram.functions.get("__init");
    if (tmp != null) {
      currentBlock = tmp.body.get(tmp.body.size() - 1);
      currentBlock.addInst(new Ret(null));
      currentBlock = irProgram.functions.get("main").body.get(0);
      currentBlock.addInst(0, new Call(null, "__init"));
    }
  }

  public void visit(FuncDefNode node) {
    currentScope = new IRScope(currentScope);
    String tmp_name = node.name;
    if (currentScope.className != null)
      tmp_name = currentScope.className + ".." + tmp_name;
    var func = new IRFunction(transType(new ExprType(node.retType)), tmp_name);
    irProgram.functions.put(tmp_name, func);
    currentBlock = func.body.get(0);

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
    ArrayList<String> names = new ArrayList<>();
    for (var member : node.varDef_list) {
      for (int i = 0; i < member.varList.size(); ++i) {
        memberTypes.add(transType(new ExprType(member.type)));
        names.add(member.varList.get(i).first);
      }
    }
    irProgram.structs.put(node.name, new StructDef(node.name, memberTypes, names));
    currentScope = new IRScope(currentScope, node.name);

    if (node.classBuild != null) node.classBuild.accept(this);
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
          if (tmp.isTrue)
            irProgram.globals.put(pair.first, new GlobalVarDef(ptr, "1", varType));
          else if (tmp.isFalse)
            irProgram.globals.put(pair.first, new GlobalVarDef(ptr, "0", varType));
          else
            irProgram.globals.put(pair.first, new GlobalVarDef(ptr, ((AtomExprNode) pair.second).identifier, varType));
          continue;
        }
        irProgram.globals.put(pair.first, new GlobalVarDef(ptr, null, varType));

        // do initialization
        if (!irProgram.functions.containsKey("__init")) irProgram.functions.put("__init", new IRFunction(new IRType("void"), "__init"));
        currentBlock = irProgram.functions.get("__init").body.get(0);
        pair.second.accept(this);
        currentBlock.addInst(new Store(lastExpr.value, ptr));
      }
    } else { // in local scope
      for (var pair : node.varList) {
        currentScope.addVar(pair.first);
        int no = currentBlock.parent.idCnt.getOrDefault(pair.first, 0);
        currentBlock.parent.idCnt.put(pair.first, ++no);
        LocalPtr ptr = new LocalPtr(pair.first + "." + no);
        currentBlock.addInst(new Alloca(ptr, varType));
        if (pair.second != null) { // initial value
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
      tmp.paras.add(new LocalVar(new IRType("ptr"), "this"));
    for (var para : node.paras) {
      // i32 %a
      tmp.paras.add(new LocalVar(transType(new ExprType(para.first)), para.second));
    }

    for (var para : tmp.paras) {
      currentScope.addVar(para.getName());
      int no = currentBlock.parent.idCnt.getOrDefault(para.getName(), 0);
      currentBlock.parent.idCnt.put(para.getName(), ++no);
      var ptr = new LocalPtr(para.getName() + "." + no);
      // add: %a.1 = alloca i32
      currentBlock.addInst(new Alloca(ptr, para.getType()));
      // add: store i32 %a, ptr %a.1
      currentBlock.addInst(new Store(para, ptr));
    }
  }
  public void visit(ClassBuildNode node) {
    var func = new IRFunction(new IRType("void"), currentScope.className + ".." + node.name);
    func.paras.add(new LocalVar(new IRType("ptr"), "this"));
    irProgram.functions.put(currentScope.className + ".." + node.name, func);
    currentScope = new IRScope(currentScope);
    currentBlock = func.body.get(0);

    currentScope.addVar("this");
    func.idCnt.put("this", 1);
    var ptr = new LocalPtr("this.1");
    currentBlock.addInst(new Alloca(ptr, new IRType("ptr")));
    currentBlock.addInst(new Store(new LocalVar(new IRType("ptr"), "this"), ptr));

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
    if (node.type.isFunc) {
      // ! check if it is a member function
      FuncInfo tmp = null;
      if (currentScope.className != null) {
        var classInfo = globalScope.getClassDecl(currentScope.className);
        if (classInfo.getFunc(node.identifier) != null)
          tmp = new FuncInfo(currentScope.className + ".." + node.identifier, transType(new ExprType(node.type.funcDecl.retType)), new LocalPtr("this.1"));
      }
      if (tmp == null) tmp = new FuncInfo(node.identifier, transType(new ExprType(node.type.funcDecl.retType)), null);
      lastExpr = new ExprVar(null, null, tmp);
      return;
    }
    if (node.isLeftValue) {
      // ! 应该先看局部变量！再看class内的成员变量，最后看全局变量
      int no = currentBlock.parent.idCnt.getOrDefault(node.identifier, 0);
      IRVariable ptr = null;
      if (no > 0) {
        ptr = new LocalPtr(node.identifier + "." + no);
      } else if (currentScope.className != null) {
        ptr = new LocalVar(new IRType("ptr"), String.valueOf(currentBlock.parent.varCnt++));
        var this_tmp = new LocalVar(new IRType("ptr"), String.valueOf(currentBlock.parent.varCnt++));
        currentBlock.addInst(new Load(this_tmp, new LocalPtr("this.1")));
        var inst = new GetElementPtr((LocalVar) ptr, "%class." + currentScope.className, this_tmp, new IRLiteral("0", new IRType("i32")));
        inst.indexs.add(new IRLiteral(String.valueOf(irProgram.structs.get(currentScope.className).getIndexOf(node.identifier)), new IRType("i32")));
        currentBlock.addInst(inst);
      } else {
        ptr = new GlobalPtr(node.identifier);
      }

      var value = new LocalVar(transType(node.type), String.valueOf(currentBlock.parent.varCnt++));
      currentBlock.addInst(new Load(value, ptr));
      lastExpr = new ExprVar(value, ptr, null);
      return;
    }
    if (node.isStringConst) {
      GlobalPtr ptr = new GlobalPtr("string." + irProgram.stringLiteralCnt++);
      irProgram.stringLiterals.add(new StringLiteralDef(ptr, node.identifier));
      lastExpr = new ExprVar(ptr, null, null);
    } else {
      var type = transType(node.type);
      if (node.isTrue)
        lastExpr = new ExprVar(new IRLiteral("1", type), null, null);
      else if (node.isFalse)
        lastExpr = new ExprVar(new IRLiteral("0", type), null, null);
      else
        lastExpr = new ExprVar(new IRLiteral(node.identifier, type), null, null);
    }
  }
  public void visit(BinaryExprNode node) {

    node.lhs.accept(this);
    var lhsVar = lastExpr.value;

    node.rhs.accept(this);
    var rhsVar = lastExpr.value;

    LocalVar retVar = new LocalVar(transType(node.type), String.valueOf(currentBlock.parent.varCnt++));
    if (node.lhs.type.dim == 0 && node.lhs.type.isString && !node.op.equals("==") && !node.op.equals("!=")) {
      if (node.op.equals("+")) {
        currentBlock.addInst(new Call(retVar, "_string_concat", lhsVar, rhsVar));
      } else {
        var tmp = new LocalVar(new IRType("i32"), String.valueOf(currentBlock.parent.varCnt++));
        currentBlock.addInst(new Call(tmp, "_string_compare", lhsVar, rhsVar));
        switch (node.op) {
          case ">" -> currentBlock.addInst(new Icmp(retVar, ">", tmp, new IRLiteral("0", new IRType("i32"))));
          case ">=" -> currentBlock.addInst(new Icmp(retVar, ">=", tmp, new IRLiteral("0", new IRType("i32"))));
          case "<" -> currentBlock.addInst(new Icmp(retVar, "<", tmp, new IRLiteral("0", new IRType("i32"))));
          case "<=" -> currentBlock.addInst(new Icmp(retVar, "<=", tmp, new IRLiteral("0", new IRType("i32"))));
          default -> throw new RuntimeException("unknown string operator");
        }
      }
      lastExpr = new ExprVar(retVar, null, null);
      return;
    }

    if (node.op.equals("==") || node.op.equals("!=") || node.op.equals(">") || node.op.equals(">=") || node.op.equals("<") || node.op.equals("<=")) {
      currentBlock.addInst(new Icmp(retVar, node.op, lhsVar, rhsVar));
    } else {
      currentBlock.addInst(new Binary(retVar, node.op, lhsVar, rhsVar));
    }
    lastExpr = new ExprVar(retVar, null, null);
  }
  public void visit(UnaryExprNode node) { // all right value
    node.expr.accept(this);

    var tmp = new LocalVar(transType(node.type), String.valueOf(currentBlock.parent.varCnt++));
    switch (node.op) {
      case "!" -> {
        currentBlock.addInst(new Binary(tmp, "^", lastExpr.value, new IRLiteral("1", new IRType("i1"))));
        lastExpr = new ExprVar(tmp, null, null);
      }
      case "~" -> {
        currentBlock.addInst(new Binary(tmp, "^", lastExpr.value, new IRLiteral("-1", new IRType("i32"))));
        lastExpr = new ExprVar(tmp, null, null);
      }
      case "++", "--" -> {
        currentBlock.addInst(new Binary(tmp, node.op.equals("++") ? "+" : "-", lastExpr.value, new IRLiteral("1", new IRType("i32"))));
        currentBlock.addInst(new Store(tmp, lastExpr.destptr));
        lastExpr = new ExprVar(lastExpr.value, null, null);
      }
      case "+", "-" -> {
        currentBlock.addInst(new Binary(tmp, node.op, new IRLiteral("0", new IRType("i32")), lastExpr.value));
        lastExpr = new ExprVar(tmp, null, null);
      }
      default -> throw new RuntimeException("unknown unary operator");
    }
  }
  public void visit(PreSelfExprNode node) {
    node.expr.accept(this);
    var tmp = new LocalVar(transType(node.type), String.valueOf(currentBlock.parent.varCnt++));
    currentBlock.addInst(new Binary(tmp, node.op.equals("++") ? "+" : "-", lastExpr.value, new IRLiteral("1", new IRType("i32"))));
    currentBlock.addInst(new Store(tmp, lastExpr.destptr));
    lastExpr = new ExprVar(tmp, lastExpr.destptr, null);
  }
  public void visit(AssignExprNode node) {
    node.lhs.accept(this);
    var dst = lastExpr.destptr;
    if (dst == null) throw new RuntimeException("assign to a right value");
    node.rhs.accept(this);

    if (node.lhs.type.dim == 0 && node.lhs.type.isString) {
      var tmp = new LocalVar(new IRType("ptr"), String.valueOf(currentBlock.parent.varCnt++));
      currentBlock.addInst(new Call(tmp, "_string_copy", lastExpr.value));
      currentBlock.addInst(new Store(tmp, dst));
    } else {
      currentBlock.addInst(new Store(lastExpr.value, dst));
    }
    lastExpr = new ExprVar(lastExpr.value, null, null);
  }
  public void visit(CallExprNode node){
    node.func.accept(this);
    if (lastExpr.funcInfo == null) {
      // array.size(): the ret value(i32) is already stored in lastExpr.value
      return;
    }
    Call ret = null;
    if (lastExpr.funcInfo.retType.isVoid) {
      ret = new Call(null, lastExpr.funcInfo.name);
    }
    else {
      var result = new LocalVar(lastExpr.funcInfo.retType, String.valueOf(currentBlock.parent.varCnt++));
      ret = new Call(result, lastExpr.funcInfo.name);
    }

    if (lastExpr.funcInfo.thisptr != null)
      ret.args.add(lastExpr.funcInfo.thisptr);
    for (var arg : node.args) {
      arg.accept(this);
      ret.args.add(lastExpr.value);
    }
    currentBlock.addInst(ret);

    if (ret.result == null)
      lastExpr = null;
    else
      lastExpr = new ExprVar(ret.result, null, null);
  }
  public void visit(ArrayExprNode node) {
    node.array.accept(this);
    var array = (IRVariable) lastExpr.value;
    node.index.accept(this);
    var type = transType(node.type);

    LocalVar retptr = new LocalVar(new IRType("ptr"), String.valueOf(currentBlock.parent.varCnt++));
    currentBlock.addInst(new GetElementPtr(retptr, type.toString(), array, lastExpr.value));

    LocalVar ret = new LocalVar(type, String.valueOf(currentBlock.parent.varCnt++));
    currentBlock.addInst(new Load(ret, retptr));

    lastExpr = new ExprVar(ret, retptr, null);
  }
  public void visit(MemberExprNode node) {
    node.obj.accept(this);
    if (node.obj.type.dim > 0) { // 数组.size
      LocalVar tmp = new LocalVar(new IRType("ptr"), String.valueOf(currentBlock.parent.varCnt++));
      currentBlock.addInst(new GetElementPtr(tmp, "i32", (LocalVar) lastExpr.value, new IRLiteral("-1", new IRType("i32"))));
      var ret = new LocalVar(new IRType("i32"), String.valueOf(currentBlock.parent.varCnt++));
      currentBlock.addInst(new Load(ret, tmp));
      lastExpr = new ExprVar(ret, null, null);
    } else {
      var classInfo = globalScope.getClassDecl(node.obj.type.name);
      var memFunc = classInfo.getFunc(node.member);
      if (memFunc != null) {
        if (!(lastExpr.value instanceof LocalVar))
          throw new RuntimeException("this is not a pointer");
        lastExpr = new ExprVar(null, null, new FuncInfo((classInfo.name.equals("string")) ? "_string_" + memFunc.name : classInfo.name + ".." + memFunc.name,
                transType(new ExprType(memFunc.retType)), (LocalVar) lastExpr.value));
        return;
      }

      var memVar = classInfo.getVar(node.member);
      if (memVar == null) throw new RuntimeException("no such member");

      var tmp = new LocalVar(new IRType("ptr"), String.valueOf(currentBlock.parent.varCnt++));
      var inst = new GetElementPtr(tmp, "%class." + classInfo.name, (LocalVar) lastExpr.value, new IRLiteral("0", new IRType("i32")));
      inst.indexs.add(new IRLiteral(String.valueOf(irProgram.structs.get(classInfo.name).getIndexOf(node.member)), new IRType("i32")));
      currentBlock.addInst(inst);
      var value = new LocalVar(transType(new ExprType(memVar)), String.valueOf(currentBlock.parent.varCnt++));
      currentBlock.addInst(new Load(value, tmp));
      lastExpr = new ExprVar(value, tmp, null);
    }
  }

  private LocalVar NewArray(ArrayList<IREntity> sizes, int layer, BaseType type) {
    if (layer == sizes.size() - 1 && (type == null || !type.isClass)) {
      var size = sizes.get(layer);
      LocalVar ret = new LocalVar(new IRType("ptr"), String.valueOf(currentBlock.parent.varCnt++));
      currentBlock.addInst(new Call(ret, "_malloc_array", new IRLiteral((type != null && type.isBool) ? "1" : "4", new IRType("i32")), size));
      return ret;
    }
    if (layer == sizes.size()) {
      if (type == null || !type.isClass) throw new RuntimeException("array dimension error");
      var ret = new LocalVar(new IRType("ptr"), String.valueOf(currentBlock.parent.varCnt++));
      currentBlock.addInst(new Alloca(ret, type.name));
      if (globalScope.getClassDecl(type.name).hasBuildFunc())
        currentBlock.addInst(new Call(null, type.name + ".." + type.name, ret));
      return ret;
    }

    var size = sizes.get(layer);
    LocalVar ret = new LocalVar(new IRType("ptr"), String.valueOf(currentBlock.parent.varCnt++));
    currentBlock.addInst(new Call(ret, "_malloc_array", new IRLiteral("4", new IRType("i32")), size));

    // for.init: int i = 0
    int no = currentBlock.parent.forCnt++;
    var cnt = new LocalVar(new IRType("ptr"), String.valueOf(currentBlock.parent.varCnt++)); // store i
    currentBlock.addInst(new Alloca(cnt, new IRType("i32")));
    currentBlock.addInst(new Store(new IRLiteral("0", new IRType("i32")), cnt));
    currentBlock.addInst(new Jump("for.cond." + no));

    // for.cond: i < size
    currentBlock = currentBlock.parent.addBlock("for.cond." + no);
    var cntValue = new LocalVar(new IRType("i32"), String.valueOf(currentBlock.parent.varCnt++)); // load i
    currentBlock.addInst(new Load(cntValue, cnt));
    var cmp = new LocalVar(new IRType("i1"), String.valueOf(currentBlock.parent.varCnt++));
    currentBlock.addInst(new Icmp(cmp, "<", cntValue, size));
    currentBlock.addInst(new Br(cmp, "for.body." + no, "for.end." + no));

    // for.body: ret[i] = NewArray(nextSize, elementSize)
    currentBlock = currentBlock.parent.addBlock("for.body." + no);
    if (!type.isString) {
      var nextptr = new LocalVar(new IRType("ptr"), String.valueOf(currentBlock.parent.varCnt++));
      currentBlock.addInst(new GetElementPtr(nextptr, "ptr", ret, cntValue));
      // recursive call
      var nxtValue = NewArray(sizes,layer + 1, type); // must be ptr
      currentBlock.addInst(new Store(nxtValue, nextptr));
    }
    currentBlock.addInst(new Jump("for.step." + no));

    // for.step: i++
    currentBlock = currentBlock.parent.addBlock("for.step." + no);
    var inc = new LocalVar(new IRType("i32"), String.valueOf(currentBlock.parent.varCnt++));
    currentBlock.addInst(new Binary(inc, "+", cntValue, new IRLiteral("1", new IRType("i32"))));
    currentBlock.addInst(new Store(inc, cnt));
    currentBlock.addInst(new Jump("for.cond." + no));

    // for.end
    currentBlock = currentBlock.parent.addBlock("for.end." + no);
    return ret;
  }

  public void visit(NewExprNode node) {
    if (node.type.dim == 0) {
      // single class-type variable
      var tmp = new LocalVar(new IRType("ptr"), String.valueOf(currentBlock.parent.varCnt++));
      currentBlock.addInst(new Alloca(tmp, node.type.name));
      if (globalScope.getClassDecl(node.type.name).hasBuildFunc())
        currentBlock.addInst(new Call(null, node.type.name + ".." + node.type.name, tmp));
      lastExpr = new ExprVar(tmp, null, null);
      return;
    }
    var sizes = new ArrayList<IREntity>();
    for (var size : node.exprList) {
      size.accept(this);
      sizes.add(lastExpr.value);
    }
    if (node.varType.dim == sizes.size())
      lastExpr = new ExprVar(NewArray(sizes, 0, node.varType), null, null);
    else if (node.varType.dim > sizes.size())
      lastExpr = new ExprVar(NewArray(sizes, 0, null), null, null);
    else throw new RuntimeException("array dimension error");
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
