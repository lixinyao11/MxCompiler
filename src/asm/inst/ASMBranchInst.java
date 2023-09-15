package asm.inst;

import asm.operand.*;
import asm.section.ASMBlock;

import java.util.HashSet;

public class ASMBranchInst extends ASMInst {
  Register rs1 = null, rs2 = null;
  Label dst = null;
  String op = null;

  public ASMBranchInst(ASMBlock parent, String op, Register rs1, Register rs2, Label dst) {
    super(parent);
    this.op = op;
    this.rs1 = rs1;
    this.rs2 = rs2;
    this.dst = dst;
  }

  @Override
  public String toString() {
    String tmp = switch (op) {
      case "==" -> "beq";
      case "!=" -> "bne";
      case "<" -> "blt";
      case ">" -> "bgt";
      case "<=" -> "ble";
      case ">=" -> "bge";
      default -> throw new RuntimeException("ASMBranchInst: unknown op: " + op);
    };
    return String.format("%-8s", (rs2 == null) ? tmp + "z" : tmp) + " " + rs1 + ", " + ((rs2 == null) ? "" : (rs2 + ", ")) + dst;
  }
  @Override
  public void initUseDef(HashSet<VirtualRegister> use, HashSet<VirtualRegister> def) {
    if (rs1 instanceof VirtualRegister && !def.contains(rs1))
      use.add((VirtualRegister) rs1);
    if (rs2 != null && rs2 instanceof VirtualRegister && !def.contains(rs2))
      use.add((VirtualRegister) rs2);
  }
}
