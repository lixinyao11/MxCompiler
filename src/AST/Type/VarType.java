package AST.Type;

public class VarType extends BaseType {
  public int dim = 0;

  public VarType(String baseType, int dim) {
    super(baseType);
    this.dim = dim;
  }
  public VarType(BaseType other) {
    super(other);
    if (other instanceof VarType) {
      this.dim = ((VarType) other).dim;
    }
  }

  @Override
  public boolean equals(BaseType obj) {
    VarType other = new VarType(obj);
    return super.equals(obj) && dim == other.dim;
  }

  @Override
  public String toString() {
    return super.toString() + "[]".repeat(Math.max(0, dim));
  }
  
}
