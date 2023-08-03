package Util.Scope;

public class LoopScope extends Scope {

  public LoopScope(Scope parent) {
    super(parent);
  }

  @Override
  public boolean isInLoop() {
    return true;
  }
  
}
