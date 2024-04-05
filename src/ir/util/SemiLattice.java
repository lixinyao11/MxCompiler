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
    
}
