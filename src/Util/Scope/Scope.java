package Util.Scope;

import java.util.HashMap;
import Util.Type;

public class Scope {
  Scope parentScope = null;
  HashMap<String, Type> varDefs = null;

  public Scope(Scope parent) {
    this.parentScope = parent;
    this.varDefs = new HashMap<>();
  }
  
}
