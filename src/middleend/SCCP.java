package middleend;

import java.util.HashMap;
import java.util.HashSet;

import ir.IRProgram;
import ir.inst.IRBinaryInst;
import ir.inst.IRCallInst;
import ir.inst.IRGetElementPtrInst;
import ir.inst.IRIcmpInst;
import ir.inst.IRInst;
import ir.inst.IRLoadInst;
import ir.inst.IRPhiInst;
import ir.module.*;
import ir.util.*;
import ir.util.entity.*;

public class SCCP {
    IRProgram program = null;

    public SCCP(IRProgram program) {
        this.program = program;
    }
    
    public void work() {
        program.functions.forEach((key, func) -> {
            workOnFunc(func);
        });
    }

    // & 先过一遍 SSCP。再遍历所有 br 指令，找到不可达的 block，
    // & 在 CFG 上处理，删除所有不可达的 block，并修改其他 block 的 phi
    // & 注意：在 Inline 之前，所有非本 block 中的 SSA name 只会通过 phi 传递给下面的 block，其他地方不会出现
    // & inline 传递函数实参时，可能会在函数体中直接用到之前的 SSA name（函数参数）
    // & 但如果 call inline callee 的那个 block 不可达了的话，那接下来的整个 callee 都不可达了，也就不存在这个问题了
    private void workOnFunc(IRFuncDef func) {
        SCCP(func);
    }

    private void SCCP(IRFuncDef func) {
        // initialize
        HashSet<LocalVar> worklist = new HashSet<>();
        HashMap<LocalVar, SemiLattice> SSAValue = new HashMap<>();
        func.body.forEach(block -> {
            block.phiInsts.forEach((key, phi) -> {
                SemiLattice value = InitValue(phi);
                SSAValue.put(phi.result, value);
                if (value.value != LatticeValue.TOP) {
                    worklist.add(phi.result);
                }
            });
            block.instructions.forEach(stmt -> {
                LocalVar var = null;
                if (stmt instanceof IRBinaryInst binaryInst) {
                    var = binaryInst.result;
                } else if (stmt instanceof IRCallInst callInst) {
                    var = callInst.result;
                } else if (stmt instanceof IRGetElementPtrInst getElementPtrInst) {
                    var = getElementPtrInst.result;
                } else if (stmt instanceof IRIcmpInst icmpInst) {
                    var = icmpInst.result;
                } else if (stmt instanceof IRLoadInst loadInst) {
                    var = loadInst.result;
                }
                if (var != null) {
                    SemiLattice value = InitValue(stmt);
                    SSAValue.put(var, value);
                    if (value.value != LatticeValue.TOP) {
                        worklist.add(var);
                    }
                }
            });
        });

        // TODO: propagate
    }

    private SemiLattice InitValue(IRInst inst) {
        if (inst instanceof IRPhiInst) {
            return new SemiLattice(0);
        } else if (inst instanceof IRBinaryInst binaryInst) {
            if (binaryInst.rhs1 instanceof IRLiteral lhs && binaryInst.rhs1.getType().isInt && 
                binaryInst.rhs2 instanceof IRLiteral rhs && binaryInst.rhs2.getType().isInt) {
                int l = lhs.getIntValue();
                int r = rhs.getIntValue();
                int ans = 0;
                if ((binaryInst.op.equals("/") || binaryInst.op.equals("%")) && r == 0) 
                    return new SemiLattice(new IRLiteral(String.valueOf(ans), new IRType("i32")));
                ans = switch (binaryInst.op) {
                    case "==" -> l == r ? 1 : 0;
                    case "!=" -> l != r ? 1 : 0;
                    case ">" -> l > r ? 1 : 0;
                    case ">=" -> l >= r ? 1 : 0;
                    case "<" -> l < r ? 1 : 0;
                    case "<=" -> l <= r ? 1 : 0;
                    case "+" -> l + r;
                    case "-" -> l - r;
                    case "*" -> l * r;
                    case "/" -> l / r;
                    case "%" -> l % r;
                    case "<<" -> l << r;
                    case ">>" -> l >> r;
                    case "&" -> l & r;
                    case "^" -> l ^ r;
                    case "|" -> l | r;
                    default -> throw new RuntimeException("unknown binary operator");
                };
                return new SemiLattice(new IRLiteral(String.valueOf(ans), new IRType("i32")));
            }
            return new SemiLattice(0);
        } else if (inst instanceof IRCallInst) {
            // ~ 由于目前不考虑过程间常量传播，因此所有 call 外部函数的结果都是 BOTTOM
            // ~ 如果 call 的是 builtin 函数，也同样认为是 BOTTOM（不考虑一些全局 ptr 为常量的情况）
            return new SemiLattice(1);
        } else if (inst instanceof IRGetElementPtrInst) {
            // ~ 同样暂不考虑 ptr 的常量传播，认为可变
            return new SemiLattice(1);
        } else if (inst instanceof IRIcmpInst cmpInst) {
            if (cmpInst.rhs1 instanceof IRLiteral lhs && cmpInst.rhs1.getType().isInt && 
                cmpInst.rhs2 instanceof IRLiteral rhs && cmpInst.rhs2.getType().isInt) {
                int l = lhs.getIntValue();
                int r = rhs.getIntValue();
                int ans = switch (cmpInst.op) {
                    case "==" -> l == r ? 1 : 0;
                    case "!=" -> l != r ? 1 : 0;
                    case ">" -> l > r ? 1 : 0;
                    case ">=" -> l >= r ? 1 : 0;
                    case "<" -> l < r ? 1 : 0;
                    case "<=" -> l <= r ? 1 : 0;
                    default -> throw new RuntimeException("unknown icmp operator");
                };
                return new SemiLattice(new IRLiteral(String.valueOf(ans), new IRType("i1")));
            }
            return new SemiLattice(0);
        } else if (inst instanceof IRLoadInst) {
            // ~ 记得如果是 globalptr 是可能 const 的，暂不处理
            return new SemiLattice(1);
        } else {
            throw new RuntimeException("inst type without return value, not supported in SCCP");
        }
    }
}