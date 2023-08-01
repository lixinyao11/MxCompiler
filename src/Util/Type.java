package Util;

// import java.util.HashMap;

public class Type {
  public boolean isInt = false, isBool = false, isString = false;
  public String name; // for class
  public int dim = 0; // dim = 0: not array
  
  public Type(String typeName, int dim) {
    this.dim = dim;
    if (typeName == "int") {
      isInt = true;
    } else if (typeName == "bool") {
      isBool = true;
    } else if (typeName == "string") {
      isString = true;
    } else {
      name = typeName;
    }
  }

  // consider dim when comparing
  public boolean equals(Type other) {
    if (dim != other.dim)
      return false;
    if (isInt && other.isInt)
      return true;
    if (isBool && other.isBool)
      return true;
    if (isString && other.isString)
      return true;
    if (name != null && other.name != null && name.equals(other.name))
      return true;
    return false;
  }


}
