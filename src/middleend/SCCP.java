package middleend;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

import ir.*;
import ir.inst.*;
import ir.module.*;
import ir.util.*;
import ir.util.entity.*;

public class SCCP {
    IRProgram program = null;
    HashSet<String> worklist = null;
    HashMap<String, SemiLattice> SSAValue = null;

    public SCCP(IRProgram program) {
        this.program = program;
        this.worklist = new HashSet<>();
        this.SSAValue = new HashMap<>();
    }
    
    public void work() {
        program.functions.forEach((key, func) -> {
            // System.err.println("SCCP on function " + key);
            workOnFunc(func);
        });
    }

    // & 先过一遍 SSCP。再遍历所有 br 指令，找到不可达的 block，
    // & 在 CFG 上处理，删除所有不可达的 block，并修改其他 block 的 phi
    // & 注意：在 Inline 之前，所有非本 block 中的 SSA name 只会通过 phi 传递给下面的 block，其他地方不会出现
    // & inline 传递函数实参时，可能会在函数体中直接用到之前的 SSA name（函数参数）
    // & 但如果 call inline callee 的那个 block 不可达了的话，那接下来的整个 callee 都不可达了，也就不存在这个问题了
    private void workOnFunc(IRFuncDef func) {
        worklist = new HashSet<>();
        SSAValue = new HashMap<>();

        SSCP(func);

        // 遍历所有指令。如果 result 是 const，就删除该 inst。如果操作数中有 const，就用 IRLiteral 替换
        // 对 br 指令，如果 i1 是 const，就修改为 jump，并修改 CFG 的前驱后继
        func.body.forEach(block -> {
            HashMap<String, IRPhiInst> newPhis = new HashMap<>();
            block.phiInsts.forEach((key, phi) -> {
                if (!checkConst(phi)) newPhis.put(key, phi);
            });
            block.phiInsts = newPhis;

            ArrayList<IRInst> newInsts = new ArrayList<>();
            block.instructions.forEach(stmt -> {
                // 整个 block 唯一的（最后的）一个 exitInst
                if (stmt instanceof IRBrInst br) {
                    if (isConst(br.cond) != null) {
                        int ans = isConst(br.cond).getIntValue();
                        assert ans == 0 || ans == 1;
                        IRJumpInst jump = new IRJumpInst(block, ans == 1 ? br.thenBlock : br.elseBlock);
                        newInsts.add(jump);
                        IRBlock unreachBlock = ans == 1 ? br.elseBlock : br.thenBlock;
                        block.succs.remove(unreachBlock);
                        unreachBlock.preds.remove(block);
                    } else {
                        newInsts.add(stmt);
                    }
                } else if (!checkConst(stmt)) {
                    newInsts.add(stmt);
                }
                
            });
            block.instructions = newInsts;
        });

        // 没有前驱的 block 应该被直接删除（除了entry），前驱减少了的 block 需要修改 phi
        ArrayList<IRBlock> newBody = new ArrayList<>();
        func.body.forEach(block -> {
            // ! entry 不能删！
            if (block.label.equals("entry")) {
                newBody.add(block);
                return;
            } else if (block.preds.size() == 0) {
                // 需要删除，也要修改前驱后继
                block.succs.forEach(succ -> {
                    succ.preds.remove(block);
                });
            }
            if (block.preds.size() > 0) {
                block.phiInsts.forEach((key, phi) -> {
                    for (int i = 0; i < phi.blocks.size(); i++) {
                        if (!block.preds.contains(phi.blocks.get(i))) {
                            phi.values.remove(i);
                            phi.blocks.remove(i);
                            i--;
                        }
                    }
                });
                newBody.add(block);
            }
        });
        func.body = newBody;

        // ~ 如果只有一句 return 且返回值是 const，且全程无 print/input，直接返回
        IRRetInst only_return = null;
        boolean const_flag = false;
        boolean io_flag = false;
        for (IRBlock block : func.body) {
            for (IRInst inst : block.instructions) {
                if (inst instanceof IRCallInst call) {
                    if (call.funcName.equals("printf") || call.funcName.equals("sprintf") || 
                        call.funcName.equals("print") || call.funcName.equals("println") ||
                        call.funcName.equals("printInt") || call.funcName.equals("printlnInt") ||
                        call.funcName.equals("getString") || call.funcName.equals("scanf") ||
                        call.funcName.equals("toString") || call.funcName.equals("getInt")) {
                        io_flag = true;
                        break;
                    }
                }
                if (inst instanceof IRRetInst ret) {
                    if (only_return == null) {
                        only_return = ret;
                        const_flag = true;
                    } else {
                        const_flag = false;
                        break;
                    }
                }
            }
        }
        if (const_flag == true && io_flag == false && only_return.value instanceof IRLiteral) {
            func.body = new ArrayList<>();
            func.body.add(new IRBlock("entry", func, 0));
            func.body.get(0).addInst(only_return);
        }
    }

    // * 如果 inst 的 result 是 const，返回 true
    // * 如果 inst 的 操作数中有 const，用 IRLiteral 替换它
    //   只需处理 phi, binary, call, getElementPtr, icmp, ret, store
    private boolean checkConst(IRInst inst) {
        if (inst instanceof IRPhiInst phi) {
            if (isConst(phi.result) != null) return true;
            ArrayList<IREntity> newValues = new ArrayList<>();
            phi.values.forEach(value -> {
                if (value instanceof LocalVar v && isConst(v) != null) {
                    newValues.add(isConst(v));
                } else {
                    newValues.add(value);
                }
            });
            phi.values = newValues;
        } else if (inst instanceof IRBinaryInst binary) {
            if (isConst(binary.result) != null) return true;
            if (binary.rhs1 instanceof LocalVar l1 && isConst(l1) != null) 
                binary.rhs1 = isConst(l1);
            if (binary.rhs2 instanceof LocalVar l2 && isConst(l2) != null) 
                binary.rhs2 = isConst(l2);
        } else if (inst instanceof IRCallInst call) {
            // ^ call.result 一定是 BOTTOM 不是 const
            ArrayList<IREntity> newArgs = new ArrayList<>();
            call.args.forEach(arg -> {
                if (arg instanceof LocalVar var && isConst(var) != null) {
                    newArgs.add(isConst(var));
                } else {
                    newArgs.add(arg);
                }
            });
            call.args = newArgs;
        } else if (inst instanceof IRGetElementPtrInst getElementPtr) {
            // ^ getElementPtr.result 一定是 BOTTOM 不是 const
            ArrayList<IREntity> newIndexs = new ArrayList<>();
            getElementPtr.indexs.forEach(index -> {
                if (index instanceof LocalVar var && isConst(var) != null) {
                    newIndexs.add(isConst(var));
                } else {
                    newIndexs.add(index);
                }
            });
            getElementPtr.indexs = newIndexs;
        } else if (inst instanceof IRIcmpInst icmp) {
            if (isConst(icmp.result) != null) return true;
            if (icmp.rhs1 instanceof LocalVar l1 && isConst(l1) != null) 
                icmp.rhs1 = isConst(l1);
            if (icmp.rhs2 instanceof LocalVar l2 && isConst(l2) != null) 
                icmp.rhs2 = isConst(l2);
        } else if (inst instanceof IRRetInst ret) {
            if (ret.value != null && ret.value instanceof LocalVar var && isConst(var) != null) 
                ret.value = isConst(var);
        } else if (inst instanceof IRStoreInst store) {
            if (store.src instanceof LocalVar var && isConst(var) != null)
                store.src = isConst(var);
        }
        return false;
    }

    private IRLiteral isConst(LocalVar var) {
        SemiLattice value = SSAValue.get(var.name);
        if (value.value == LatticeValue.CONSTANT) return value.constant;
        return null;
    }

    private void SSCP(IRFuncDef func) {
        // initialize
        func.body.forEach(block -> {
            block.phiInsts.forEach((key, phi) -> InitValue(phi));
            block.instructions.forEach(stmt -> InitValue(stmt));
        });
        // ~ 不考虑过程间常量传播，函数的形参全部认为是 BOTTOM
        for (LocalVar para : func.paras) {
            SSAValue.put(para.name, new SemiLattice(1));
        }

        // if (func.name.equals("test")) {
        //     System.err.println("SSAValue:" + SSAValue.toString());
        //     System.err.println("worklist:" + worklist.toString());
        // }

        while (!worklist.isEmpty()) {
            String var = worklist.iterator().next();
            worklist.remove(var);
            func.body.forEach(block -> {
                block.phiInsts.forEach((key, phi) -> {
                    String m = UseVarDef(phi, var);
                    if (m != null) { // phi used var and defines m
                        SemiLattice oldValue = SSAValue.get(m);
                        if (oldValue.value != LatticeValue.BOTTOM) {
                            SemiLattice newValue = Interpretation(phi);
                            SSAValue.replace(m, newValue);
                            if (newValue != oldValue) worklist.add(m);

                            // if (func.name.equals("test")) {
                            //     System.err.println("inst:" + phi.toString());
                            //     System.err.println("SSAValue:" + SSAValue.toString());
                            //     System.err.println("worklist:" + worklist.toString());
                            // }
                        }
                    }
                });
                block.instructions.forEach(stmt -> {
                    String m = UseVarDef(stmt, var);
                    if (m != null) {
                        SemiLattice oldValue = SSAValue.get(m);
                        if (oldValue.value != LatticeValue.BOTTOM) {
                            SemiLattice newValue = Interpretation(stmt);
                            SSAValue.replace(m, newValue);
                            if (newValue != oldValue) worklist.add(m);

                            // if (func.name.equals("test")) {
                            //     System.err.println("inst:" + stmt.toString());
                            //     System.err.println("SSAValue:" + SSAValue.toString());
                            //     System.err.println("worklist:" + worklist.toString());
                            // }
                        }
                    }
                });
            });
        }
    }

    // * inst can only be IRPhiInst, IRBinaryInst, IRIcmpInst
    private SemiLattice Interpretation(IRInst inst) {
        if (inst instanceof IRPhiInst phi) {
            SemiLattice value = getLattice(phi.values.get(0));
            for (int i = 1; i < phi.values.size(); i++) {
                value = value.meet(getLattice(phi.values.get(i)));
            }
            return value;
        } else if (inst instanceof IRBinaryInst binary) {
            SemiLattice lhs = getLattice(binary.rhs1);
            SemiLattice rhs = getLattice(binary.rhs2);
            if (lhs.value == LatticeValue.TOP || rhs.value == LatticeValue.TOP) 
                return new SemiLattice(0);
            if (lhs.value == LatticeValue.CONSTANT && rhs.value == LatticeValue.CONSTANT) {
                int l = lhs.constant.getIntValue();
                int r = rhs.constant.getIntValue();
                // ^ 实际上这种语句是不可达的，会在后续处理不可达 block 中被删除
                if (r == 0 && binary.op.equals("/") || r == 0 && binary.op.equals("%")) 
                    return new SemiLattice(0);
                int ans = switch (binary.op) {
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
            // & 一些特殊情况：0 * x = 0, 0 & x = 0
            if (binary.op.equals("*") || binary.op.equals("&")) {
                if ((lhs.value == LatticeValue.CONSTANT && lhs.constant.getIntValue() == 0)
                    || (rhs.value == LatticeValue.CONSTANT && rhs.constant.getIntValue() == 0))
                    return new SemiLattice(new IRLiteral(String.valueOf(0), new IRType("i32")));
            }
            // 此时至少有一个 BOTTOM 且排除了特殊情况
            return new SemiLattice(1);
        } else if (inst instanceof IRIcmpInst icmp) {
            SemiLattice lhs = getLattice(icmp.rhs1);
            SemiLattice rhs = getLattice(icmp.rhs2);
            if (lhs.value == LatticeValue.TOP || rhs.value == LatticeValue.TOP) 
                return new SemiLattice(0);
            if (lhs.value == LatticeValue.CONSTANT && rhs.value == LatticeValue.CONSTANT) {
                int l = lhs.constant.getIntValue();
                int r = rhs.constant.getIntValue();
                int ans = switch (icmp.op) {
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
            // 此时至少有一个 BOTTOM
            return new SemiLattice(1);
        } else {
            throw new RuntimeException("inst type not supported in SSCP interpretation");
        }
    }
    
    private SemiLattice getLattice(IREntity entity) {
        if (entity instanceof LocalVar var) {
            if (!SSAValue.containsKey(var.name)) {
                System.err.println("SSAValue does not contain " + var.name);
            }
            assert SSAValue.containsKey(var.name);
            return SSAValue.get(var.name);
        }
        if (entity instanceof IRLiteral literal) return new SemiLattice(literal);
        return new SemiLattice(1); // ~ global ptr 认为不可知
    }
    
    // * check that inst used var and defines m
    // * if not, return null
    // * if m is already BOTTOM return null(call, getElementPtrInst, LoadInst)
    // * return m: inst can only be IRPhiInst, IRBinaryInst, IRIcmpInst
    private String UseVarDef(IRInst inst, String var) {
        if (inst instanceof IRPhiInst phi) {
            for (IREntity v : phi.values) {
                if (v instanceof LocalVar localv) {
                    if (localv.name.equals(var)) return phi.result.name;
                }
            }
            return null;
        } else if (inst instanceof IRBinaryInst binary) {
            if ((binary.rhs1 instanceof LocalVar l1 && l1.name.equals(var))
             || (binary.rhs2 instanceof LocalVar l2 && l2.name.equals(var))) 
             return binary.result.name;
            return null;
        } else if (inst instanceof IRIcmpInst icmp) {
            if ((icmp.rhs1 instanceof LocalVar l1 && l1.name.equals(var))
             || (icmp.rhs2 instanceof LocalVar l2 && l2.name.equals(var))) 
             return icmp.result.name;
            return null;
        } else { // inst without return value
            return null;
            // ~ callInst, getElementPtrInst, LoadInst result 一定是 BOTTOM 不考虑
        }
    }

    // * 初始化 inst 的 result ssaname 的 lattice value
    // * 把值保存进 SSAValue 中
    // * 如果不是 TOP，加入 worklist
    private void InitValue(IRInst inst) {
        if (inst instanceof IRPhiInst phi) {
            SSAValue.put(phi.result.name, new SemiLattice(0));
            return;
        } 
        if (inst instanceof IRBinaryInst binaryInst) {
            if (binaryInst.rhs1 instanceof IRLiteral lhs && binaryInst.rhs1.getType().isInt && 
                binaryInst.rhs2 instanceof IRLiteral rhs && binaryInst.rhs2.getType().isInt) {
                int l = lhs.getIntValue();
                int r = rhs.getIntValue();
                int ans = 0;
                if ((binaryInst.op.equals("/") || binaryInst.op.equals("%")) && r == 0) {
                    SSAValue.put(binaryInst.result.name, new SemiLattice(new IRLiteral(String.valueOf(ans), new IRType("i32"))));
                    worklist.add(binaryInst.result.name);
                    return;
                }
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
                SSAValue.put(binaryInst.result.name, new SemiLattice(new IRLiteral(String.valueOf(ans), new IRType("i32"))));
                worklist.add(binaryInst.result.name);
            } else {
                SSAValue.put(binaryInst.result.name, new SemiLattice(0));
            }
            return;
        } 
        if (inst instanceof IRCallInst call) {
            // ~ 由于目前不考虑过程间常量传播，因此所有 call 外部函数的结果都是 BOTTOM
            // ~ 如果 call 的是 builtin 函数，也同样认为是 BOTTOM（不考虑一些全局 ptr 为常量的情况）
            if (call.result != null) {
                SSAValue.put(call.result.name, new SemiLattice(1));
                worklist.add(call.result.name);
            }
            return;
        } 
        if (inst instanceof IRGetElementPtrInst getElement) {
            // ~ 同样暂不考虑 ptr 的常量传播，认为可变
            SSAValue.put(getElement.result.name, new SemiLattice(1));
            worklist.add(getElement.result.name);
            return;
        } 
        if (inst instanceof IRIcmpInst cmp) {
            if (cmp.rhs1 instanceof IRLiteral lhs && cmp.rhs1.getType().isInt && 
                cmp.rhs2 instanceof IRLiteral rhs && cmp.rhs2.getType().isInt) {
                int l = lhs.getIntValue();
                int r = rhs.getIntValue();
                int ans = switch (cmp.op) {
                    case "==" -> l == r ? 1 : 0;
                    case "!=" -> l != r ? 1 : 0;
                    case ">" -> l > r ? 1 : 0;
                    case ">=" -> l >= r ? 1 : 0;
                    case "<" -> l < r ? 1 : 0;
                    case "<=" -> l <= r ? 1 : 0;
                    default -> throw new RuntimeException("unknown icmp operator");
                };
                SSAValue.put(cmp.result.name, new SemiLattice(new IRLiteral(String.valueOf(ans), new IRType("i1"))));
                worklist.add(cmp.result.name);
            } else {
                SSAValue.put(cmp.result.name, new SemiLattice(0));
            }
            return;
        }
        if (inst instanceof IRLoadInst load) {
            // ~ 不考虑 globalptr 可能是 const 的情况
            SSAValue.put(load.result.name, new SemiLattice(1));
            worklist.add(load.result.name);
            return;
        }
    }
}