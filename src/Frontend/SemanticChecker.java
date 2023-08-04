package Frontend;

import AST.*;
import AST.Expr.*;
import AST.Stmt.*;
import Util.Scope.*;
import Util.Decl.*;
import Util.Type.*;
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
    } else if (!mainFunc.returnTypeIs(new ReturnType("int", 0))) {
      throw new SemanticError("Main function should return int", node.pos);
    } else if (mainFunc.calledByArgs(new ArrayList<ExprType>()) == null) {
      throw new SemanticError("Main function should have no parameter", node.pos);
    }

    for (var def : node.decls) {
      def.accept(this);
    }
  }

  public void visit(FuncDefNode node) {
    // functions already added to globalScope/classScope
    if (currentScope instanceof GlobalScope) {
      if (globalScope.getClassDecl(node.name) != null)
        throw new SemanticError("function name" + node.name + " conflicts with class name", node.pos);
    } else if (currentScope instanceof ClassScope) {
      if (currentScope.existVarName(node.name))
        throw new SemanticError("function name" + node.name + " conflicts with variable name", node.pos);
    } else {
      throw new Error("funcDef " + node.name + " is not in GlobalScope or ClassScope");
    }

    currentScope = new FuncScope(currentScope, node.retType);
    node.paraList.accept(this);
    for (var stmt : node.stmt_List) {
      if (stmt != null) stmt.accept(this);
    }
    if (!((FuncScope)currentScope).isReturned() && !node.name.equals("main"))
      throw new SemanticError("non-void function " + node.name + " does not have return statement", node.pos);
    currentScope = currentScope.parentScope;
  }

  public void visit(ClassDefNode node) {
    // currentScope must be globalScope
    currentScope = node.scope; // the variables and functions has already been added to the scope
    for (var varDef : node.varDef_list) {
      varDef.accept(this);
    }
    for (var funcDef : node.funcDef_list) {
      funcDef.accept(this);
    }
    if (node.classBuild != null) {
      // name must match
      if (!node.classBuild.name.equals(node.name))
        throw new SemanticError("classBuild name does not match", node.pos);
      node.classBuild.accept(this);
    }
    currentScope = currentScope.parentScope;
  }

  public void visit(VarDefNode node) {
    if (globalScope.getClassDecl(node.type.name) == null)
      throw new SemanticError("type " + node.type.name + " not defined", node.pos);
    for (var var : node.varList) {
      if (currentScope instanceof GlobalScope) {
        if (globalScope.getFuncDecl(var.a) != null)
          throw new SemanticError("variable name" + var.a + " conflicts with function name", node.pos);
      }
      if (var.b != null) {
        var.b.accept(this);
        if (!var.b.type.equals(node.type))
          throw new SemanticError("VarDef for type " + node.type.toString() + " is type " + var.b.type.toString(), node.pos);
      }
      if (!(currentScope instanceof ClassScope)) {
        currentScope.addVarSafe(node.type, var.a, node.pos);
      }
    }
  }

  public void visit(ParaListNode node) {
    for (var para : node.paras) {
      if (globalScope.getClassDecl(para.a.name) == null)
        throw new SemanticError("type " + para.a.name + " not defined", node.pos);
      currentScope.addVarSafe(para.a, para.b, node.pos);
    }
  }

  public void visit(ClassBuildNode node) {
    // currentScope must be classScope
    if (!(currentScope instanceof ClassScope))
      throw new SemanticError("classBuild is not in ClassScope", node.pos);
    currentScope = new FuncScope(currentScope, new ReturnType("void", 0));
    for (var stmt : node.stmt_List) {
      stmt.accept(this);
    }
    currentScope = currentScope.parentScope;
  }

  public void visit(BlockStmtNode node) {
    currentScope = new Scope(currentScope);
    for (var stmt : node.stmts) {
      if (stmt != null) stmt.accept(this);
    }
    currentScope = currentScope.parentScope;
  }

  public void visit(IfStmtNode node) {
    node.cond.accept(this);
    if (!node.cond.type.equals(new ExprType("bool", 0)))
      throw new SemanticError("Condition of if statement should be bool", node.pos);

    if (node.thenStmt != null) {
      currentScope = new Scope(currentScope);
      node.thenStmt.accept(this);
      currentScope = currentScope.parentScope;
    }

    if (node.elseStmt != null) {
      currentScope = new Scope(currentScope);
      node.elseStmt.accept(this);
      currentScope = currentScope.parentScope;
    }
  }
  public void visit(WhileStmtNode node) {
    node.cond.accept(this);
    if (!node.cond.type.equals(new ExprType("bool", 0)))
      throw new SemanticError("Condition of while statement should be bool", node.pos);

    if (node.body != null) {
      currentScope = new LoopScope(currentScope);
      node.body.accept(this);
      currentScope = currentScope.parentScope;
    }
  }

  public void visit(ForStmtNode node) {
    currentScope = new LoopScope(currentScope);
    if (node.initStmt != null) {
      node.initStmt.accept(this);
    }
    if (node.condExpr != null) {
      node.condExpr.accept(this);
      if (!node.condExpr.type.equals(new ExprType("bool", 0)))
        throw new SemanticError("Condition of for statement should be bool", node.pos);
    }
    if (node.stepExpr != null) node.stepExpr.accept(this);
    if (node.body != null)  node.body.accept(this);
    currentScope = currentScope.parentScope;
  }

  public void visit(ContinueStmtNode node) {
    if (!currentScope.isInLoop())
      throw new SemanticError("Continue statement not in a loop", node.pos);
  }

  public void visit(BreakStmtNode node) {
    if (!currentScope.isInLoop())
      throw new SemanticError("Break statement not in a loop", node.pos);
  }

  public void visit(ReturnStmtNode node) {
    if (node.retExpr != null) {
      node.retExpr.accept(this);
      currentScope.returnsType(node.retExpr.type, node.pos);
    } else {
      currentScope.returnsType(new ExprType("void", 0), node.pos);
    }
  }

  public void visit(ExprStmtNode node) {
    if (node.expr == null) throw new Error("expr is null");
    node.expr.accept(this);
    if (node.expr.type.isFunc)
      throw new SemanticError("function type can not be computed", node.pos);
  }

  public void visit(VardefStmtNode node) {
    node.varDef.accept(this);
  }

  public void visit(AtomExprNode node) {
    if (node.isIntConst) {
      node.type = new ExprType("int", 0);
      node.isLeftValue = false;
    } else if (node.isStringConst) {
      node.type = new ExprType("string", 0);
      node.isLeftValue = false;
    } else if (node.isTrue || node.isFalse) {
      node.type = new ExprType("bool", 0);
      node.isLeftValue = false;
    } else if (node.isNull) {
      node.type = new ExprType("null", 0);
      node.isLeftValue = false;
    } else if (node.isThis) {
      String className = currentScope.isInClass();
      if (className == null) {
        throw new SemanticError("this should be in class", node.pos);
      }
      node.type = new ExprType(className, 0);
      node.isLeftValue = true;
    } else if (node.isIdentifier) {
      ExprType idType = currentScope.getIdType(node.identifier);
      if (idType != null) {
        node.type = idType;
        node.isLeftValue = !idType.isFunc;
      } else {
        throw new SemanticError("identifier " + node.identifier + " not defined", node.pos);
      }
    } else {
      throw new Error("atomExprNode error");
    }

  }
  public void visit(BinaryExprNode node) {
    node.lhs.accept(this);
    node.rhs.accept(this);

    if (!node.lhs.type.equals(node.rhs.type))
      throw new SemanticError("Type mismatch in binary expression", node.pos);

    if (node.rhs.type.dim > 0 || node.lhs.type.dim > 0) {
      if (node.op.equals("==") || node.op.equals("!=")) {
        node.type = new ExprType("bool", 0);
        node.isLeftValue = false;
        return;
      }
      throw new SemanticError("array type cannot be computed", node.pos);
    }

    if (node.lhs.type.isBool) {
      if (node.op.equals("&&") || node.op.equals("||") || node.op.equals("==") || node.op.equals("!=")) {
        node.type = new ExprType("bool", 0);
        node.isLeftValue = false;
        return;
      }
      throw new SemanticError("type bool cannot be computed", node.pos);
    }

    if (node.lhs.type.isString) {
      if (node.op.equals("+")) {
        node.type = new ExprType("string", 0);
      } else if (node.op.equals("==") || node.op.equals("!=") || node.op.equals(">") || node.op.equals("<") || node.op.equals(">=") || node.op.equals("<=")) {
        node.type = new ExprType("bool", 0);
      } else {
        throw new SemanticError("String cannot be computed by " + node.op, node.pos);
      }
      node.isLeftValue = false;
      return;
    }
    if (node.op.equals(">") || node.op.equals("<") || node.op.equals(">=") || node.op.equals("<=") || node.op.equals("==") || node.op.equals("!=")) {
      node.type = new ExprType("bool", 0);
    } else if (node.op.equals("+") || node.op.equals("-") || node.op.equals("*") || node.op.equals("/") || node.op.equals("%") || node.op.equals("<<") || node.op.equals(">>") || node.op.equals("&") || node.op.equals("|") || node.op.equals("^") || node.op.equals("~")) {
      node.type = new ExprType("int", 0);
    } else {
      throw new SemanticError("Type mismatch in binary expression", node.pos);
    }
    node.isLeftValue = false;
  }

  public void visit(UnaryExprNode node) {
    node.expr.accept(this);
    switch (node.op) {
      case "++":
      case "--":
        if (!node.expr.type.equals(new ExprType("int", 0)))
          throw new SemanticError("Type mismatch in self expression", node.pos);
        if (!node.expr.isLeftValue)
          throw new SemanticError("Expression in self expression is not left value", node.pos);
        node.type = new ExprType("int", 0);
        node.isLeftValue = false;
        break;
      case "+":
      case "-":
      case "~":
        if (!node.expr.type.equals(new ExprType("int", 0)))
          throw new SemanticError("Type mismatch in unary expression '+|-|~'", node.pos);
        node.type = new ExprType("int", 0);
        node.isLeftValue = false;
        break;
      case "!":
        if (!node.expr.type.equals(new ExprType("bool", 0)))
          throw new SemanticError("Type mismatch in unary expression '!'", node.pos);
        node.type = new ExprType("bool", 0);
        node.isLeftValue = false;
        break;
      default:
        throw new Error("Unknown unary operator");
    }
  }

  public void visit(PreSelfExprNode node) {
    node.expr.accept(this);
    if (!node.expr.type.equals(new ExprType("int", 0)))
      throw new SemanticError("Type mismatch in pre-self expression", node.pos);
    if (!node.expr.isLeftValue)
      throw new SemanticError("Expression in pre-self expression is not left value", node.pos);
    node.type = new ExprType("int", 0);
    node.isLeftValue = true;
  }

  public void visit(AssignExprNode node) {
    node.lhs.accept(this);
    node.rhs.accept(this);
    if (!node.lhs.isLeftValue)
      throw new SemanticError("Left value required in assignment expression", node.pos);
    if (!node.lhs.type.equals(node.rhs.type))
      throw new SemanticError("assign type " + node.lhs.type.toString() + " with type " + node.rhs.type.toString(), node.pos);
    node.type = node.lhs.type;
    node.isLeftValue = false;
  }

  public void visit(CallExprNode node) {
    node.func.accept(this);
    ArrayList<ExprType> argTypes = new ArrayList<>();
    for (var expr : node.args) {
      expr.accept(this);
      argTypes.add(expr.type);
    }
    if (!node.func.type.isFunc)
      throw new SemanticError("call expression on non-func type", node.pos);
    var tmp = node.func.type.funcDecl.calledByArgs(argTypes);
    if (tmp == null)
      throw new SemanticError("Function call with wrong arguments", node.pos);
    node.type = new ExprType(tmp);
    node.isLeftValue = false;
  }

  public void visit(ArrayExprNode node) {
    node.array.accept(this);
    node.index.accept(this);
    if (!node.index.type.equals(new ExprType("int", 0)))
      throw new SemanticError("Array index must be int", node.pos);
    if (node.array.type.dim == 0)
      throw new SemanticError("Type mismatch in array expression", node.pos);
    node.type = new ExprType(node.array.type);
    node.type.dim--;
    node.isLeftValue = true;
  }

  public void visit(MemberExprNode node) {
    node.obj.accept(this);
    if (node.obj.type.dim > 0) {
      if (!node.member.equals("size"))
        throw new SemanticError("Member expression on array type", node.pos);
      node.type = new ExprType("size", new FuncDecl("size", "int", null, null));
      node.isLeftValue = false;
      return;
    }
    if (!node.obj.type.isString && !node.obj.type.isClass)
      throw new SemanticError("Member expression on non-class, non-string type", node.pos);
    var classDecl = globalScope.getClassDecl(node.obj.type.name);
    var memVar = classDecl.getVar(node.member);
    if (memVar != null) {
      node.type = new ExprType(memVar);
      node.isLeftValue = true;
      return;
    }
    var memFunc = classDecl.getFunc(node.member);
    if (memFunc != null) {
      node.type = new ExprType(node.obj.type.name, memFunc);
      node.isLeftValue = true;
      return;
    }
    throw new SemanticError("Member not found", node.pos);
  }

  public void visit(NewExprNode node) {
    if (node.varType.isClass) {
      if (globalScope.getClassDecl(node.varType.name) == null)
        throw new SemanticError("Class not found", node.pos);
    }
    if (node.exprList.isEmpty()) {
      if (!node.varType.isClass || node.varType.dim > 0)
        throw new SemanticError("Type mismatch in new expression", node.pos);
      node.type = new ExprType(node.varType);
      node.isLeftValue = false;
      return;
    }
    if (node.varType.dim == 0)
      throw new SemanticError("Type mismatch in new expression", node.pos);
    if (node.varType.dim < node.exprList.size())
      throw new SemanticError("Type dim mismatch in new expression", node.pos);
    for (var expr : node.exprList) {
      expr.accept(this);
      if (!expr.type.equals(new ExprType("int", 0)))
        throw new SemanticError("Type mismatch in new expression", node.pos);
    }
    node.type = new ExprType(node.varType);
    node.isLeftValue = false;
  }

  public void visit(ConditionalExprNode node) {
    node.cond.accept(this);
    node.thenExpr.accept(this);
    node.elseExpr.accept(this);
    if (!node.cond.type.equals(new ExprType("bool", 0)))
      throw new SemanticError("Type mismatch in conditional expression", node.pos);
    if (!node.thenExpr.type.equals(node.elseExpr.type))
      throw new SemanticError("Type mismatch in conditional expression", node.pos);
    node.type = node.thenExpr.type;
    node.isLeftValue = false;
  }
  
}
