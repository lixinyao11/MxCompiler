package util;

public class SemanticError extends RuntimeException {
  String msg;
  Position pos;

  public SemanticError(String msg, Position pos) {
    this.msg = msg;
    this.pos = pos;
  }
  
  @Override
  public String toString() {
    return "Semantic Error: " + msg + " at " + pos.toString();
  }
}
