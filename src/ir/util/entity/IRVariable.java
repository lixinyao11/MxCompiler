package ir.util.entity;

import ir.util.IRType;

abstract public class IRVariable extends IREntity {
  abstract public String toString();
  abstract public IRType getType();
}
