package Util.Type;

public class BaseType {
  public boolean isInt = false, isBool = false, isString = false, isClass = false;
  public String name; // for class

  public BaseType(String typeName) {
    name = typeName;
    if (typeName.equals("int")) {
      isInt = true;
    } else if (typeName.equals("bool")) {
      isBool = true;
    } else if (typeName.equals("string")) {
      isString = true;
    } else {
      throw new RuntimeException("BaseType constructor error: class without classdDecl");
    }
  }
  public BaseType(BaseType other) {
    this.isInt = other.isInt;
    this.isBool = other.isBool;
    this.isString = other.isString;
    this.isClass = other.isClass;
    this.name = other.name;
  }

  public boolean equals(BaseType other) {
    if (isInt && other.isInt)
      return true;
    if (isBool && other.isBool)
      return true;
    if (isString && other.isString)
      return true;
    if (isClass && other.isClass && name.equals(other.name))
      return true;
    return false;
  }

  public String toString() {
    if (isInt)
      return "int";
    if (isBool)
      return "bool";
    if (isString)
      return "string";
    if (isClass)
      return name;
    throw new RuntimeException("Type.toString() error");
  }

}