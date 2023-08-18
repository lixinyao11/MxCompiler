	.text
	.attribute	4, 16
	.attribute	5, "rv32i2p1"
	.file	"output.ll"
	.globl	exchange                        # -- Begin function exchange
	.p2align	2
	.type	exchange,@function
exchange:                               # @exchange
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	lui	a2, %hi(a)
	lw	a3, %lo(a)(a2)
	sw	a0, 12(sp)
	sw	a1, 8(sp)
	slli	a0, a0, 2
	add	a0, a3, a0
	lw	a4, 0(a0)
	sw	a4, 4(sp)
	slli	a1, a1, 2
	add	a1, a3, a1
	lw	a1, 0(a1)
	sw	a1, 0(a0)
	lw	a0, 8(sp)
	lw	a1, %lo(a)(a2)
	lw	a2, 4(sp)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	addi	sp, sp, 16
	ret
.Lfunc_end0:
	.size	exchange, .Lfunc_end0-exchange
	.cfi_endproc
                                        # -- End function
	.globl	main                            # -- Begin function main
	.p2align	2
	.type	main,@function
main:                                   # @main
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	sw	s1, 4(sp)                       # 4-byte Folded Spill
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	.cfi_offset s1, -12
	lui	s0, %hi(n)
	call	getString
	call	_string_parseInt
	mv	a1, a0
	sw	a0, %lo(n)(s0)
	lui	s0, %hi(a)
	li	a0, 4
	call	_malloc_array
	sw	a0, %lo(a)(s0)
	sw	zero, 0(sp)
.LBB1_1:                                # %for.cond.0
                                        # =>This Inner Loop Header: Depth=1
	lw	a0, %lo(a)(s0)
	lw	a1, 0(sp)
	lw	a0, -4(a0)
	bge	a1, a0, .LBB1_3
# %bb.2:                                # %for.body.0
                                        #   in Loop: Header=BB1_1 Depth=1
	lw	a0, 0(sp)
	lw	a1, %lo(a)(s0)
	slli	a2, a0, 2
	add	a1, a1, a2
	sw	a0, 0(a1)
	lw	a0, 0(sp)
	addi	a0, a0, 1
	sw	a0, 0(sp)
	j	.LBB1_1
.LBB1_3:                                # %for.end.0
	call	makeHeap
	call	heapSort
	sw	zero, 0(sp)
	lui	s1, %hi(a)
	lui	a0, %hi(string.0)
	addi	s0, a0, %lo(string.0)
.LBB1_4:                                # %for.cond.1
                                        # =>This Inner Loop Header: Depth=1
	lw	a0, %lo(a)(s1)
	lw	a1, 0(sp)
	lw	a0, -4(a0)
	bge	a1, a0, .LBB1_6
# %bb.5:                                # %for.body.1
                                        #   in Loop: Header=BB1_4 Depth=1
	lw	a0, 0(sp)
	lw	a1, %lo(a)(s1)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a0, 0(a0)
	call	toString
	mv	a1, s0
	call	_string_concat
	call	print
	lw	a0, 0(sp)
	addi	a0, a0, 1
	sw	a0, 0(sp)
	j	.LBB1_4
.LBB1_6:                                # %for.end.1
	lui	a0, %hi(string.1)
	addi	a0, a0, %lo(string.1)
	call	print
	li	a0, 0
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	lw	s1, 4(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end1:
	.size	main, .Lfunc_end1-main
	.cfi_endproc
                                        # -- End function
	.globl	adjustHeap                      # -- Begin function adjustHeap
	.p2align	2
	.type	adjustHeap,@function
adjustHeap:                             # @adjustHeap
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 32
	.cfi_def_cfa s0, 0
	sw	a0, -12(s0)
	sw	zero, -16(s0)
	sw	zero, -20(s0)
	sw	zero, -24(s0)
	lui	a0, %hi(a)
.LBB2_1:                                # %while.cond.0
                                        # =>This Inner Loop Header: Depth=1
	lw	a1, -16(s0)
	lw	a2, -12(s0)
	slli	a1, a1, 1
	bge	a1, a2, .LBB2_8
# %bb.2:                                # %while.body.0
                                        #   in Loop: Header=BB2_1 Depth=1
	lw	a1, -16(s0)
	lw	a2, -12(s0)
	slli	a1, a1, 1
	sw	a1, -20(s0)
	addi	a3, a1, 1
	addi	a1, sp, -16
	mv	sp, a1
	bge	a3, a2, .LBB2_4
# %bb.3:                                # %if.then.0
                                        #   in Loop: Header=BB2_1 Depth=1
	lw	a2, -16(s0)
	lw	a3, %lo(a)(a0)
	slli	a2, a2, 3
	add	a2, a3, a2
	lw	a3, 4(a2)
	lw	a2, 0(a2)
.LBB2_4:                                # %if.else.0
                                        #   in Loop: Header=BB2_1 Depth=1
	slt	a2, a3, a2
	sb	a2, 0(a1)
	lbu	a1, 0(a1)
	beqz	a1, .LBB2_6
# %bb.5:                                # %if.then.1
                                        #   in Loop: Header=BB2_1 Depth=1
	lw	a1, -16(s0)
	slli	a1, a1, 1
	addi	a1, a1, 1
	sw	a1, -20(s0)
.LBB2_6:                                # %if.end.1
                                        #   in Loop: Header=BB2_1 Depth=1
	lw	a1, -16(s0)
	lw	a2, %lo(a)(a0)
	lw	a3, -20(s0)
	slli	a1, a1, 2
	add	a1, a2, a1
	lw	a1, 0(a1)
	slli	a3, a3, 2
	add	a2, a2, a3
	lw	a2, 0(a2)
	bge	a2, a1, .LBB2_8
# %bb.7:                                # %if.then.2
                                        #   in Loop: Header=BB2_1 Depth=1
	mv	a1, sp
	addi	sp, a1, -16
	lw	a2, -16(s0)
	lw	a3, %lo(a)(a0)
	slli	a2, a2, 2
	add	a2, a3, a2
	lw	a2, 0(a2)
	lw	a3, -20(s0)
	lw	a4, %lo(a)(a0)
	sw	a2, -16(a1)
	lw	a2, -16(s0)
	slli	a3, a3, 2
	add	a3, a4, a3
	lw	a3, 0(a3)
	slli	a2, a2, 2
	add	a2, a4, a2
	sw	a3, 0(a2)
	lw	a2, -20(s0)
	lw	a3, %lo(a)(a0)
	lw	a1, -16(a1)
	slli	a2, a2, 2
	add	a2, a3, a2
	sw	a1, 0(a2)
	lw	a1, -20(s0)
	sw	a1, -16(s0)
	j	.LBB2_1
.LBB2_8:                                # %while.end.0
	li	a0, 0
	addi	sp, s0, -32
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end2:
	.size	adjustHeap, .Lfunc_end2-adjustHeap
	.cfi_endproc
                                        # -- End function
	.globl	heapSort                        # -- Begin function heapSort
	.p2align	2
	.type	heapSort,@function
heapSort:                               # @heapSort
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	sw	s1, 20(sp)                      # 4-byte Folded Spill
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	.cfi_offset s1, -12
	sw	zero, 16(sp)
	sw	zero, 12(sp)
	lui	s0, %hi(n)
	lui	s1, %hi(a)
.LBB3_1:                                # %for.cond.0
                                        # =>This Inner Loop Header: Depth=1
	lw	a0, 12(sp)
	lw	a1, %lo(n)(s0)
	bge	a0, a1, .LBB3_3
# %bb.2:                                # %for.body.0
                                        #   in Loop: Header=BB3_1 Depth=1
	lw	a0, %lo(a)(s1)
	lw	a1, 0(a0)
	lw	a2, 12(sp)
	lw	a3, %lo(n)(s0)
	sw	a1, 16(sp)
	not	a1, a2
	add	a1, a1, a3
	slli	a1, a1, 2
	add	a1, a0, a1
	lw	a1, 0(a1)
	sw	a1, 0(a0)
	lw	a0, 12(sp)
	lw	a1, %lo(n)(s0)
	lw	a2, %lo(a)(s1)
	not	a0, a0
	lw	a3, 16(sp)
	add	a0, a0, a1
	slli	a0, a0, 2
	add	a0, a2, a0
	sw	a3, 0(a0)
	lw	a0, 12(sp)
	lw	a1, %lo(n)(s0)
	not	a0, a0
	add	a0, a0, a1
	call	adjustHeap
	lw	a0, 12(sp)
	addi	a0, a0, 1
	sw	a0, 12(sp)
	j	.LBB3_1
.LBB3_3:                                # %for.end.0
	li	a0, 0
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	lw	s1, 20(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end3:
	.size	heapSort, .Lfunc_end3-heapSort
	.cfi_endproc
                                        # -- End function
	.globl	makeHeap                        # -- Begin function makeHeap
	.p2align	2
	.type	makeHeap,@function
makeHeap:                               # @makeHeap
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	sw	s1, 20(sp)                      # 4-byte Folded Spill
	sw	s2, 16(sp)                      # 4-byte Folded Spill
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	.cfi_offset s1, -12
	.cfi_offset s2, -16
	addi	s0, sp, 32
	.cfi_def_cfa s0, 0
	lui	s1, %hi(n)
	lw	a0, %lo(n)(s1)
	addi	a0, a0, -1
	srli	a1, a0, 31
	add	a0, a0, a1
	srai	a0, a0, 1
	sw	a0, -20(s0)
	sw	zero, -24(s0)
	sw	zero, -28(s0)
	lui	s2, %hi(a)
	j	.LBB4_2
.LBB4_1:                                # %if.end.2
                                        #   in Loop: Header=BB4_2 Depth=1
	lw	a0, -20(s0)
	addi	a0, a0, -1
	sw	a0, -20(s0)
.LBB4_2:                                # %while.cond.0
                                        # =>This Inner Loop Header: Depth=1
	lw	a0, -20(s0)
	bltz	a0, .LBB4_9
# %bb.3:                                # %while.body.0
                                        #   in Loop: Header=BB4_2 Depth=1
	lw	a0, -20(s0)
	lw	a1, %lo(n)(s1)
	slli	a0, a0, 1
	sw	a0, -28(s0)
	addi	a2, a0, 1
	addi	a0, sp, -16
	mv	sp, a0
	bge	a2, a1, .LBB4_5
# %bb.4:                                # %if.then.0
                                        #   in Loop: Header=BB4_2 Depth=1
	lw	a1, -20(s0)
	lw	a2, %lo(a)(s2)
	slli	a1, a1, 3
	add	a1, a2, a1
	lw	a2, 4(a1)
	lw	a1, 0(a1)
.LBB4_5:                                # %if.else.0
                                        #   in Loop: Header=BB4_2 Depth=1
	slt	a1, a2, a1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB4_7
# %bb.6:                                # %if.then.1
                                        #   in Loop: Header=BB4_2 Depth=1
	lw	a0, -20(s0)
	slli	a0, a0, 1
	addi	a0, a0, 1
	sw	a0, -28(s0)
.LBB4_7:                                # %if.end.1
                                        #   in Loop: Header=BB4_2 Depth=1
	lw	a0, -20(s0)
	lw	a1, %lo(a)(s2)
	lw	a2, -28(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a0, 0(a0)
	slli	a2, a2, 2
	add	a1, a1, a2
	lw	a1, 0(a1)
	bge	a1, a0, .LBB4_1
# %bb.8:                                # %if.then.2
                                        #   in Loop: Header=BB4_2 Depth=1
	lw	a0, -20(s0)
	lw	a1, -28(s0)
	call	exchange
	j	.LBB4_1
.LBB4_9:                                # %while.end.0
	li	a0, 0
	addi	sp, s0, -32
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	lw	s1, 20(sp)                      # 4-byte Folded Reload
	lw	s2, 16(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end4:
	.size	makeHeap, .Lfunc_end4-makeHeap
	.cfi_endproc
                                        # -- End function
	.type	a,@object                       # @a
	.section	.sbss,"aw",@nobits
	.globl	a
	.p2align	2
a:
	.word	0
	.size	a, 4

	.type	n,@object                       # @n
	.globl	n
	.p2align	2
n:
	.word	0                               # 0x0
	.size	n, 4

	.type	string.0,@object                # @string.0
	.section	.rodata,"a",@progbits
	.globl	string.0
string.0:
	.asciz	" "
	.size	string.0, 2

	.type	string.1,@object                # @string.1
	.globl	string.1
string.1:
	.asciz	"\\n"
	.size	string.1, 3

	.section	".note.GNU-stack","",@progbits
