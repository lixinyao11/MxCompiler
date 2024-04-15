package ir.util;

import ir.util.entity.IRLiteral;

public class SemiLattice {
    public LatticeValue value;
    public IRLiteral constant;

    public SemiLattice(int n) {
        if (n == 0) {
            this.value = LatticeValue.TOP;
        } else {
            this.value = LatticeValue.BOTTOM;
        }
        this.constant = null;
    }

    public SemiLattice(IRLiteral constant) {
        this.value = LatticeValue.CONSTANT;
        this.constant = constant;
    }

    public SemiLattice meet(SemiLattice other) {
        if (this.value == LatticeValue.TOP) 
            return other;
        if (other.value == LatticeValue.TOP) 
            return this;
        if (this.value == LatticeValue.BOTTOM) 
            return this;
        if (other.value == LatticeValue.BOTTOM)
            return other;
        if (this.constant.type.isInt && other.constant.type.isInt 
            && this.constant.getIntValue() == other.constant.getIntValue()) 
            return this;

        return new SemiLattice(1); // 如果 !literal.isInt，就认为不相等      
    }

    public String toString() {
        if (this.value == LatticeValue.TOP) 
            return "TOP";
        if (this.value == LatticeValue.BOTTOM) 
            return "BOTTOM";
        return this.constant.toString();
    }
    
}
