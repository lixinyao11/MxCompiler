package util;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Position {
  private final int row, column;

  public Position(int row, int column) {
    this.row = row;
    this.column = column;
  }

  public Position(Token token) {
    this.row = token.getLine();
    this.column = token.getCharPositionInLine();
  }

  public Position(ParserRuleContext ctx) {
    this.row = ctx.getStart().getLine();
    this.column = ctx.getStart().getCharPositionInLine();
  }

  public Position(TerminalNode node) {
    this.row = node.getSymbol().getLine();
    this.column = node.getSymbol().getCharPositionInLine();
  }

  public String toString() {
    return "(" + row + "," + column + ")";
  }
  
}
