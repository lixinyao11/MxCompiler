package Util.Type;

public class VarType extends BaseType {
  public int dim = 0;

  public VarType(String baseType, int dim) {
    super(baseType);
    this.dim = dim;
  }
  public VarType(VarType other) {
    super(other.name);
    this.dim = other.dim;
  }

  @Override
  public boolean equals(BaseType obj) {
    if (!(obj instanceof VarType)) {
      if (dim == 0 && super.equals(obj))
        return true;
      return false;
    }
    return super.equals(obj) && dim == ((VarType) obj).dim;
  }
  
}
