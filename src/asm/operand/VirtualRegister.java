package asm.operand;

public class VirtualRegister extends Register {
  public static int cnt = 0; // = number of VRs
  int id = 0; // 0-based

  public VirtualRegister() {
    id = cnt++;
  }

  public static void reset() {
    cnt = 0;
  }
  @Override
  public String toString() {
    return "vr" + id;
  }
}
