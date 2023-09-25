package util;

public class Pair<T1, T2> {
  public T1 first = null;
  public T2 second = null;

  public Pair(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }

  public boolean equals(Object obj) {
    assert obj instanceof Pair;
    return first.equals(((Pair<T1, T2>) obj).first) && second.equals(((Pair<T1, T2>) obj).second);
  }

}
