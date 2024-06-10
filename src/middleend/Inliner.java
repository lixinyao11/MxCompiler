package middleend;

import ir.*;
import ir.module.*;
import ir.inst.*;
import ir.util.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Inliner {
    IRProgram program = null;
    int inline_cnt = 0;
    HashMap<String, IREntity> argMap = new HashMap<>(); // callee.paras.name -> caller.args
    HashMap<IRBlock, IRBlock> blockMap = new HashMap<>();
    String calleeName = null;

    public Inliner(IRProgram program) {
        this.program = program;
    }

    public void work() {
        count_calling_times();
        program.functions.forEach((key, func) -> {
            workOnFunc(func);
        });
        program.functions.entrySet().removeIf(entry -> entry.getValue().called_times == 0 
            && !entry.getValue().name.equals("main"));
    }

    public void workOnFunc(IRFuncDef func) {
        // System.err.println("Working on function " + func.name);
        // System.err.println("Calling times: " + func.calling_times + ", Called times: " + func.called_times);
        if (func.calling_times == 0) {
            return;
        }
        ArrayList<IRBlock> newBlocks = new ArrayList<>(); // 放新的 blocks
        func.body.forEach(block -> {
            newBlocks.add(workOnBlock(block, newBlocks, func));
        });
        func.body = newBlocks;
    }

    private IRBlock workOnBlock(IRBlock block, ArrayList<IRBlock> newBlocks, IRFuncDef func) {
        for (IRInst inst : block.instructions) {
            if (inst instanceof IRCallInst callInst) {
                if (program.functions.containsKey(callInst.funcName)) {
                    IRFuncDef callee = program.functions.get(callInst.funcName);
                    if (callee.calling_times == 0) {
                        return inline(block, callInst, newBlocks, callee, func);
                    }
                }
            }
        }

        return block; // 只有一个inline都没有才会从这里返回：什么都不做
    }

    // 先把 callInst 之前的 insts 组成的 block 加入 newBlocks,把 callInst 之后的 insts 加入 retBlock
    // inline 过程：维护两个 map，遍历 calleeFunc，生成新的 block 并加入 newBlocks，同时做好 block 之间的连接
    // 调用并返回 workOnBlock(retBlock)，对后面的指令部分进行检查
    private IRBlock inline(IRBlock block, IRCallInst callInst, ArrayList<IRBlock> newBlocks, IRFuncDef calleeFunc, IRFuncDef callerFunc) {
        // System.err.println("Inlining " + callInst.funcName + " into " + func.name);
        calleeFunc.called_times--;
        callerFunc.calling_times--;
        argMap = new HashMap<>(); // callee.paras -> caller.args
        for (int i = 0; i < callInst.args.size(); i++) {
            // & var 不是引用，其他语句里的para不是同一个，必须用name当key
            argMap.put(calleeFunc.paras.get(i).name, callInst.args.get(i));
        }
        // System.err.println("argMap: " + argMap.size());
        // argMap.forEach((key, value) -> {
            // System.err.println(key.toString() + " -> " + value.toString());
        // });
        blockMap = new HashMap<>(); // callee.body -> caller.newblocks
        calleeName = calleeFunc.name;

        IRBlock retBlock = new IRBlock(calleeName + ".ret." + inline_cnt, callerFunc, block.loopDepth);
        retBlock.instructions.addAll(block.instructions.subList(block.instructions.indexOf(callInst) + 1, block.instructions.size()));

        // System.err.println("block: " + block.label + ", succs: " + block.succs.size());
        retBlock.succs = new HashSet<>(block.succs);
        block.succs.clear();
        // ! 更换后继，意味着那些后继的前驱也要换！它们的phiInst对应的block也要换
        // System.err.println("block: " + retBlock.label + ", succs: " + retBlock.succs.size());
        for (IRBlock succ : retBlock.succs) {
            succ.preds.remove(block);
            succ.preds.add(retBlock);
            // System.err.println("succ: " + succ.label + ", preds: " + succ.preds.size());
            for (IRPhiInst phiInst : succ.phiInsts.values()) {
                int idx = phiInst.blocks.indexOf(block);
                if (idx != -1) {
                    // System.err.println("phiInst: " + phiInst.result.name + ", idx: " + idx);
                    phiInst.blocks.set(idx, retBlock);
                }
            }
        }

        IRPhiInst retPhi = null;
        if (callInst.result != null) {
            retPhi = new IRPhiInst(retBlock, callInst.result, callInst.result.getType());
        }

        block.instructions.subList(block.instructions.indexOf(callInst), block.instructions.size()).clear();
        newBlocks.add(block);

        

        // maintain blockMap
        for (IRBlock calleeBlock : calleeFunc.body) {
            IRBlock newBlock = new IRBlock(calleeBlock.label + '.' + calleeName + '.' + inline_cnt, callerFunc, calleeBlock.loopDepth);
            blockMap.put(calleeBlock, newBlock);

            // 如果是 entry block 需要和原 block 连接
            if (calleeBlock.label == "entry") {
                block.succs.add(newBlock);
                newBlock.preds.add(block);
                // 每个block至少应该有一个exitInst：要补上
                block.instructions.add(new IRJumpInst(block, newBlock));
            }
            // 如果是 exit block 需要和 ret block 连接
            if (calleeBlock.succs.isEmpty()) {
                newBlock.succs.add(retBlock);
                retBlock.preds.add(newBlock);
            }
            
            newBlocks.add(newBlock);
        }

        // newBlocks.forEach(Block -> {
        //     System.err.println(Block.label);
        // });
        
        // copy instructions and maintain block connections
        for (IRBlock calleeBlock : calleeFunc.body) {
            IRBlock newBlock = blockMap.get(calleeBlock);
            // 先 connect entry block
            if (calleeBlock.label.equals("entry")) {
                for (IRBlock succ : calleeBlock.succs) {
                    assert blockMap.containsKey(succ);
                    newBlock.succs.add(blockMap.get(succ));
                    blockMap.get(succ).preds.add(newBlock);
                }
            } else {
                // connect blocks：只看 preds
                for (IRBlock pred : calleeBlock.preds) {
                    assert blockMap.containsKey(pred);
                    newBlock.preds.add(blockMap.get(pred));
                    blockMap.get(pred).succs.add(newBlock);
                }
            }
            // ! phiInsts 不在 instructions 里！要先单独处理
            for (IRInst inst : calleeBlock.phiInsts.values()) {
                IRPhiInst phiInst = (IRPhiInst) copyInst(inst, block);
                newBlock.phiInsts.put(phiInst.result.name, phiInst);
            }
            for (IRInst inst : calleeBlock.instructions) {
                if (inst instanceof IRRetInst retInst) {
                    newBlock.instructions.add(new IRJumpInst(block, retBlock));
                    if (retPhi != null) {
                        assert retInst.value != null;
                        retPhi.addBranch(getNewEntity(retInst.value), newBlock);
                    }
                } else {
                    newBlock.instructions.add(copyInst(inst, block));
                }
            }
        }

        if (retPhi != null) retBlock.phiInsts.put(retPhi.result.name, retPhi);
        inline_cnt++;
        return workOnBlock(retBlock, newBlocks, callerFunc);
    }

    private IRInst copyInst(IRInst inst, IRBlock parent) {
        if (inst instanceof IRAllocaInst) {
            throw new RuntimeException("appears alloca in after mem2reg");
        } else if (inst instanceof IRBinaryInst binaryInst) {
            return new IRBinaryInst(parent, getNewVar(binaryInst.result), binaryInst.op, 
                getNewEntity(binaryInst.rhs1), getNewEntity(binaryInst.rhs2));
        } else if (inst instanceof IRBrInst brInst) {
            return new IRBrInst(parent, getNewVar(brInst.cond), 
                blockMap.get(brInst.thenBlock), blockMap.get(brInst.elseBlock));
        } else if (inst instanceof IRCallInst callInst) {
            IRCallInst ret = new IRCallInst(parent, getNewVar(callInst.result), callInst.funcName);
            for (IREntity arg : callInst.args) {
                ret.args.add(getNewEntity(arg));
            }
            return ret;
        } else if (inst instanceof IRGetElementPtrInst ptrInst) {
            IRGetElementPtrInst ret = new IRGetElementPtrInst(parent, getNewVar(ptrInst.result), 
                ptrInst.type, (IRVariable) getNewEntity(ptrInst.ptr), getNewEntity(ptrInst.indexs.get(0)));
            for (int i = 1; i < ptrInst.indexs.size(); i++) {
                ret.indexs.add(getNewEntity(ptrInst.indexs.get(i)));
            }
            return ret;
        } else if (inst instanceof IRIcmpInst icmpInst) {
            return new IRIcmpInst(parent, getNewVar(icmpInst.result), icmpInst.op, 
                getNewEntity(icmpInst.rhs1), getNewEntity(icmpInst.rhs2));
        } else if (inst instanceof IRJumpInst jumpInst) {
            return new IRJumpInst(parent, blockMap.get(jumpInst.destBlock));
        } else if (inst instanceof IRLoadInst loadInst) {
            return new IRLoadInst(parent, getNewVar(loadInst.result), (IRVariable) getNewEntity(loadInst.ptr));
        } else if (inst instanceof IRPhiInst phiInst) {
            IRPhiInst ret = new IRPhiInst(parent, getNewVar(phiInst.result), phiInst.type);
            for (int i = 0; i < phiInst.values.size(); i++) {
                ret.addBranch(getNewEntity(phiInst.values.get(i)), blockMap.get(phiInst.blocks.get(i)));
            }
            return ret;
        } else if (inst instanceof IRStoreInst storeInst) {
            return new IRStoreInst(parent, getNewEntity(storeInst.src), (IRVariable) getNewEntity(storeInst.dest));
        } else {
            throw new RuntimeException("unknown inst");
        }
    }

    private LocalVar getNewVar(LocalVar var) {
        if (var == null) {
            return null;
        }
        if (argMap.containsKey(var.name)) {
            return (LocalVar) argMap.get(var.name);
        } else {
            return new LocalVar(var.getType(), var.name + '.' + calleeName + '.' + inline_cnt);
        }
    }

    private IREntity getNewEntity(IREntity var) {
        if (var == null) {
            return null;
        }
        // System.err.println("getNewEntity: " + var.toString());
        if (var instanceof LocalVar var_) {
            if (argMap.containsKey(var_.name)) {
                // System.err.println("getNewEntity find: " + argMap.get(var_.name).toString());
                return argMap.get(var_.name);
            } else {
                return new LocalVar(var_.getType(), var_.name + '.' + calleeName + '.' + inline_cnt);
            }
        } else {
            return var;
        }
    }

    private void count_calling_times(){
        program.functions.forEach((key, func) -> {
            func.body.forEach(block -> {
                block.instructions.forEach(inst -> {
                    if (inst instanceof ir.inst.IRCallInst) {
                        ir.inst.IRCallInst callInst = (ir.inst.IRCallInst) inst;
                        if (program.functions.containsKey(callInst.funcName)) {
                            program.functions.get(callInst.funcName).called_times++;
                            func.calling_times++;
                        }
                    }
                });
            });
        });
    }
}
