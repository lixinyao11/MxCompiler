import asm.ASMProgram;
import backend.*;
import frontend.*;
import middleend.*;
import parser.*;
import util.scope.*;
import util.*;
import ir.*;

// import java.io.InputStream;
// import java.io.PrintStream;
// import java.io.FileInputStream;
// import java.io.FileOutputStream;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import ast.ProgramNode;
public class Main {
  public static void main(String[] args) throws Exception {
    // CharStream input = CharStreams.fromStream(new FileInputStream("input.mx"));
    CharStream input = CharStreams.fromStream(System.in);
    MxLexer lexer = new MxLexer(input);
    lexer.removeErrorListeners();
     lexer.addErrorListener(new MxErrorListener());
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MxParser parser = new MxParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(new MxErrorListener());
    ParseTree tree = parser.program();
    ASTBuilder astBuilder = new ASTBuilder();
    ProgramNode ast = (ProgramNode) astBuilder.visit(tree);
    GlobalScope globalScope = new GlobalScope();
    new SymbolCollector(globalScope).visit(ast);
    new SemanticChecker(globalScope).visit(ast);
    // AST -> LLVM IR
    IRProgram irProgram = new IRProgram();
    new IRBuilder(irProgram, globalScope).visit(ast);
    new IROptimize(irProgram).work();
//    System.out.write(irProgram.toString().getBytes());
    ASMProgram asmProgram = new ASMProgram();
    new InstSelection(asmProgram).visit(irProgram);
    new RegAllocation(asmProgram).work();
    System.out.write(asmProgram.toString().getBytes());
  }
}