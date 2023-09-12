package backend;

import asm.inst.ASMInst;
import asm.operand.Immediate;
import asm.operand.MemAddr;
import asm.operand.Register;
import asm.section.ASMBlock;

import java.util.ArrayList;
import java.util.HashMap;
/*
public class FuncManager {
  // * Integer represents the number, referring to the position in stack
  HashMap<String, Integer> virtualRegister = null;
  HashMap<String, Register> paraRegister = null; // a0-a7
  HashMap<String, ASMInst> paraOffset = null; // paras more than 8
  public int calleeRegCnt = 0, callerRegCnt = 0; // 是个数不是下标
  public int stackCnt = 0; // virtualregs和callerRegs的个数
  public ArrayList<ASMInst> retInsts = null;

  public FuncManager() {
    virtualRegister = new HashMap<>();
    paraOffset = new HashMap<>();
    paraRegister = new HashMap<>();
    retInsts = new ArrayList<>();
  }

  public MemAddr addVirtualReg(String name) {
    virtualRegister.put(name, stackCnt);
    return new MemAddr(new Immediate((stackCnt++) * 4), new Register("sp"));
  }

  public MemAddr getAddr(String name) {
    return new MemAddr(new Immediate(virtualRegister.get(name) * 4), new Register("sp"));
  }

  public void updateCalleeReg(int cnt) {
    calleeRegCnt = Math.max(calleeRegCnt, cnt);
  }

  public MemAddr saveCallerReg() {
    callerRegCnt++;
    return new MemAddr(new Immediate((stackCnt++) * 4), new Register("sp"));
  }

  public int getStackSize() {
    return (stackCnt + calleeRegCnt) * 4;
  }
} */
