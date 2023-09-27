package util;

public class Pair<T1, T2> {
  public T1 first = null;
  public T2 second = null;

  public Pair(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public boolean equals(Object obj) {
    assert obj instanceof Pair;
    Pair<T1, T2> tmp = (Pair<T1, T2>) obj;
    return first == tmp.first && second == tmp.second;
  }

  @Override
  public int hashCode() {
    return first.hashCode() ^ second.hashCode();
  }

}
