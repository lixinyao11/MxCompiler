	.text
	.attribute	4, 16
	.attribute	5, "rv32i2p1"
	.file	"output.ll"
	.globl	main                            # -- Begin function main
	.p2align	2
	.type	main,@function
main:                                   # @main
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -80
	.cfi_def_cfa_offset 80
	sw	ra, 76(sp)                      # 4-byte Folded Spill
	sw	s0, 72(sp)                      # 4-byte Folded Spill
	sw	s1, 68(sp)                      # 4-byte Folded Spill
	sw	s2, 64(sp)                      # 4-byte Folded Spill
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	.cfi_offset s1, -12
	.cfi_offset s2, -16
	addi	s0, sp, 80
	.cfi_def_cfa s0, 0
	call	getInt
	sw	a0, -20(s0)
	sw	zero, -24(s0)
	sw	zero, -44(s0)
	sw	zero, -28(s0)
	sw	zero, -32(s0)
	addi	a1, a0, -1
	sw	a1, -36(s0)
	sw	a1, -40(s0)
	sw	zero, -56(s0)
	sw	zero, -60(s0)
	sw	zero, -52(s0)
	sw	zero, -48(s0)
	mv	a1, a0
	call	__mulsi3
	mv	a1, a0
	li	a0, 4
	call	_malloc_array
	sw	a0, -64(s0)
	sw	zero, -76(s0)
.LBB0_1:                                # %for.cond.0
                                        # =>This Inner Loop Header: Depth=1
	lw	s1, -76(s0)
	lw	a0, -20(s0)
	mv	a1, a0
	call	__mulsi3
	bge	s1, a0, .LBB0_3
# %bb.2:                                # %for.body.0
                                        #   in Loop: Header=BB0_1 Depth=1
	lw	a0, -76(s0)
	lw	a1, -64(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	zero, 0(a0)
	lw	a0, -76(s0)
	addi	a0, a0, 1
	sw	a0, -76(s0)
	j	.LBB0_1
.LBB0_3:                                # %for.end.0
	lw	a0, -20(s0)
	mv	a1, a0
	call	__mulsi3
	mv	a1, a0
	li	a0, 4
	call	_malloc_array
	sw	a0, -68(s0)
	sw	zero, -76(s0)
.LBB0_4:                                # %for.cond.1
                                        # =>This Inner Loop Header: Depth=1
	lw	s1, -76(s0)
	lw	a0, -20(s0)
	mv	a1, a0
	call	__mulsi3
	bge	s1, a0, .LBB0_6
# %bb.5:                                # %for.body.1
                                        #   in Loop: Header=BB0_4 Depth=1
	lw	a0, -76(s0)
	lw	a1, -68(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	zero, 0(a0)
	lw	a0, -76(s0)
	addi	a0, a0, 1
	sw	a0, -76(s0)
	j	.LBB0_4
.LBB0_6:                                # %for.end.1
	lw	a1, -20(s0)
	li	a0, 8
	call	_malloc_array
	sw	a0, -72(s0)
	sw	zero, -76(s0)
	li	s1, -1
	j	.LBB0_8
.LBB0_7:                                # %for.end.3
                                        #   in Loop: Header=BB0_8 Depth=1
	lw	a0, -76(s0)
	addi	a0, a0, 1
	sw	a0, -76(s0)
.LBB0_8:                                # %for.cond.2
                                        # =>This Loop Header: Depth=1
                                        #     Child Loop BB0_10 Depth 2
	lw	a0, -76(s0)
	lw	a1, -20(s0)
	bge	a0, a1, .LBB0_12
# %bb.9:                                # %for.body.2
                                        #   in Loop: Header=BB0_8 Depth=1
	lw	a0, -76(s0)
	lw	a2, -72(s0)
	lw	a1, -20(s0)
	slli	a0, a0, 2
	add	s2, a2, a0
	li	a0, 4
	call	_malloc_array
	sw	a0, 0(s2)
	sw	zero, -80(s0)
.LBB0_10:                               # %for.cond.3
                                        #   Parent Loop BB0_8 Depth=1
                                        # =>  This Inner Loop Header: Depth=2
	lw	a0, -80(s0)
	lw	a1, -20(s0)
	bge	a0, a1, .LBB0_7
# %bb.11:                               # %for.body.3
                                        #   in Loop: Header=BB0_10 Depth=2
	lw	a0, -76(s0)
	lw	a1, -72(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a1, -80(s0)
	lw	a0, 0(a0)
	slli	a1, a1, 2
	add	a0, a0, a1
	sw	s1, 0(a0)
	lw	a0, -80(s0)
	addi	a0, a0, 1
	sw	a0, -80(s0)
	j	.LBB0_10
.LBB0_12:                               # %for.end.2
	lw	a0, -64(s0)
	lw	a1, -28(s0)
	sw	a1, 0(a0)
	lw	a0, -68(s0)
	lw	a1, -32(s0)
	sw	a1, 0(a0)
	lw	a0, -28(s0)
	lw	a1, -72(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a1, -32(s0)
	lw	a0, 0(a0)
	slli	a1, a1, 2
	add	a0, a0, a1
	sw	zero, 0(a0)
	li	s1, 1
.LBB0_13:                               # %while.cond.0
                                        # =>This Inner Loop Header: Depth=1
	lw	a0, -24(s0)
	lw	a1, -44(s0)
	blt	a1, a0, .LBB0_88
# %bb.14:                               # %while.body.0
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -24(s0)
	lw	a1, -64(s0)
	slli	a0, a0, 2
	add	a1, a1, a0
	lw	a2, 0(a1)
	lw	a3, -72(s0)
	lw	a4, -68(s0)
	slli	a2, a2, 2
	add	a2, a3, a2
	add	a4, a4, a0
	lw	a0, 0(a4)
	lw	a2, 0(a2)
	slli	a0, a0, 2
	add	a0, a2, a0
	lw	a0, 0(a0)
	sw	a0, -52(s0)
	lw	a0, 0(a1)
	addi	a0, a0, -1
	sw	a0, -56(s0)
	lw	a2, 0(a4)
	lw	a1, -20(s0)
	addi	a2, a2, -2
	sw	a2, -60(s0)
	call	check
	andi	a0, a0, 1
	addi	s2, sp, -16
	mv	sp, s2
	beqz	a0, .LBB0_16
# %bb.15:                               # %if.then.0
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -60(s0)
	lw	a1, -20(s0)
	call	check
	andi	a0, a0, 1
.LBB0_16:                               # %if.else.0
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a0, 0(s2)
	lbu	a1, 0(s2)
	addi	a0, sp, -16
	mv	sp, a0
	beqz	a1, .LBB0_18
# %bb.17:                               # %if.then.1
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -56(s0)
	lw	a2, -72(s0)
	slli	a1, a1, 2
	add	a1, a2, a1
	lw	a2, -60(s0)
	lw	a1, 0(a1)
	slli	a2, a2, 2
	add	a1, a1, a2
	lw	a1, 0(a1)
	addi	a1, a1, 1
	seqz	a1, a1
.LBB0_18:                               # %if.else.1
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_23
# %bb.19:                               # %if.then.2
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -44(s0)
	addi	a0, a0, 1
	lw	a1, -64(s0)
	lw	a2, -56(s0)
	sw	a0, -44(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -44(s0)
	lw	a1, -68(s0)
	lw	a2, -60(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -56(s0)
	lw	a1, -72(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a1, -60(s0)
	lw	a0, 0(a0)
	lw	a2, -52(s0)
	slli	a1, a1, 2
	add	a0, a0, a1
	addi	a2, a2, 1
	sw	a2, 0(a0)
	lw	a1, -56(s0)
	lw	a2, -36(s0)
	addi	a0, sp, -16
	mv	sp, a0
	bne	a1, a2, .LBB0_21
# %bb.20:                               # %if.then.3
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -60(s0)
	lw	a2, -40(s0)
.LBB0_21:                               # %if.else.3
                                        #   in Loop: Header=BB0_13 Depth=1
	xor	a1, a1, a2
	seqz	a1, a1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_23
# %bb.22:                               # %if.then.4
                                        #   in Loop: Header=BB0_13 Depth=1
	sw	s1, -48(s0)
.LBB0_23:                               # %if.end.2
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -24(s0)
	lw	a1, -64(s0)
	slli	a2, a0, 2
	add	a1, a1, a2
	lw	a0, 0(a1)
	lw	a1, -68(s0)
	addi	a0, a0, -1
	sw	a0, -56(s0)
	add	a1, a1, a2
	lw	a2, 0(a1)
	lw	a1, -20(s0)
	addi	a2, a2, 2
	sw	a2, -60(s0)
	call	check
	andi	a0, a0, 1
	addi	s2, sp, -16
	mv	sp, s2
	beqz	a0, .LBB0_25
# %bb.24:                               # %if.then.5
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -60(s0)
	lw	a1, -20(s0)
	call	check
	andi	a0, a0, 1
.LBB0_25:                               # %if.else.5
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a0, 0(s2)
	lbu	a1, 0(s2)
	addi	a0, sp, -16
	mv	sp, a0
	beqz	a1, .LBB0_27
# %bb.26:                               # %if.then.6
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -56(s0)
	lw	a2, -72(s0)
	slli	a1, a1, 2
	add	a1, a2, a1
	lw	a2, -60(s0)
	lw	a1, 0(a1)
	slli	a2, a2, 2
	add	a1, a1, a2
	lw	a1, 0(a1)
	addi	a1, a1, 1
	seqz	a1, a1
.LBB0_27:                               # %if.else.6
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_32
# %bb.28:                               # %if.then.7
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -44(s0)
	addi	a0, a0, 1
	lw	a1, -64(s0)
	lw	a2, -56(s0)
	sw	a0, -44(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -44(s0)
	lw	a1, -68(s0)
	lw	a2, -60(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -56(s0)
	lw	a1, -72(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a1, -60(s0)
	lw	a0, 0(a0)
	lw	a2, -52(s0)
	slli	a1, a1, 2
	add	a0, a0, a1
	addi	a2, a2, 1
	sw	a2, 0(a0)
	lw	a1, -56(s0)
	lw	a2, -36(s0)
	addi	a0, sp, -16
	mv	sp, a0
	bne	a1, a2, .LBB0_30
# %bb.29:                               # %if.then.8
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -60(s0)
	lw	a2, -40(s0)
.LBB0_30:                               # %if.else.8
                                        #   in Loop: Header=BB0_13 Depth=1
	xor	a1, a1, a2
	seqz	a1, a1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_32
# %bb.31:                               # %if.then.9
                                        #   in Loop: Header=BB0_13 Depth=1
	sw	s1, -48(s0)
.LBB0_32:                               # %if.end.7
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -24(s0)
	lw	a1, -64(s0)
	slli	a2, a0, 2
	add	a1, a1, a2
	lw	a0, 0(a1)
	lw	a1, -68(s0)
	addi	a0, a0, 1
	sw	a0, -56(s0)
	add	a1, a1, a2
	lw	a2, 0(a1)
	lw	a1, -20(s0)
	addi	a2, a2, -2
	sw	a2, -60(s0)
	call	check
	andi	a0, a0, 1
	addi	s2, sp, -16
	mv	sp, s2
	beqz	a0, .LBB0_34
# %bb.33:                               # %if.then.10
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -60(s0)
	lw	a1, -20(s0)
	call	check
	andi	a0, a0, 1
.LBB0_34:                               # %if.else.10
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a0, 0(s2)
	lbu	a1, 0(s2)
	addi	a0, sp, -16
	mv	sp, a0
	beqz	a1, .LBB0_36
# %bb.35:                               # %if.then.11
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -56(s0)
	lw	a2, -72(s0)
	slli	a1, a1, 2
	add	a1, a2, a1
	lw	a2, -60(s0)
	lw	a1, 0(a1)
	slli	a2, a2, 2
	add	a1, a1, a2
	lw	a1, 0(a1)
	addi	a1, a1, 1
	seqz	a1, a1
.LBB0_36:                               # %if.else.11
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_41
# %bb.37:                               # %if.then.12
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -44(s0)
	addi	a0, a0, 1
	lw	a1, -64(s0)
	lw	a2, -56(s0)
	sw	a0, -44(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -44(s0)
	lw	a1, -68(s0)
	lw	a2, -60(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -56(s0)
	lw	a1, -72(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a1, -60(s0)
	lw	a0, 0(a0)
	lw	a2, -52(s0)
	slli	a1, a1, 2
	add	a0, a0, a1
	addi	a2, a2, 1
	sw	a2, 0(a0)
	lw	a1, -56(s0)
	lw	a2, -36(s0)
	addi	a0, sp, -16
	mv	sp, a0
	bne	a1, a2, .LBB0_39
# %bb.38:                               # %if.then.13
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -60(s0)
	lw	a2, -40(s0)
.LBB0_39:                               # %if.else.13
                                        #   in Loop: Header=BB0_13 Depth=1
	xor	a1, a1, a2
	seqz	a1, a1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_41
# %bb.40:                               # %if.then.14
                                        #   in Loop: Header=BB0_13 Depth=1
	sw	s1, -48(s0)
.LBB0_41:                               # %if.end.12
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -24(s0)
	lw	a1, -64(s0)
	slli	a2, a0, 2
	add	a1, a1, a2
	lw	a0, 0(a1)
	lw	a1, -68(s0)
	addi	a0, a0, 1
	sw	a0, -56(s0)
	add	a1, a1, a2
	lw	a2, 0(a1)
	lw	a1, -20(s0)
	addi	a2, a2, 2
	sw	a2, -60(s0)
	call	check
	andi	a0, a0, 1
	addi	s2, sp, -16
	mv	sp, s2
	beqz	a0, .LBB0_43
# %bb.42:                               # %if.then.15
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -60(s0)
	lw	a1, -20(s0)
	call	check
	andi	a0, a0, 1
.LBB0_43:                               # %if.else.15
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a0, 0(s2)
	lbu	a1, 0(s2)
	addi	a0, sp, -16
	mv	sp, a0
	beqz	a1, .LBB0_45
# %bb.44:                               # %if.then.16
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -56(s0)
	lw	a2, -72(s0)
	slli	a1, a1, 2
	add	a1, a2, a1
	lw	a2, -60(s0)
	lw	a1, 0(a1)
	slli	a2, a2, 2
	add	a1, a1, a2
	lw	a1, 0(a1)
	addi	a1, a1, 1
	seqz	a1, a1
.LBB0_45:                               # %if.else.16
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_50
# %bb.46:                               # %if.then.17
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -44(s0)
	addi	a0, a0, 1
	lw	a1, -64(s0)
	lw	a2, -56(s0)
	sw	a0, -44(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -44(s0)
	lw	a1, -68(s0)
	lw	a2, -60(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -56(s0)
	lw	a1, -72(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a1, -60(s0)
	lw	a0, 0(a0)
	lw	a2, -52(s0)
	slli	a1, a1, 2
	add	a0, a0, a1
	addi	a2, a2, 1
	sw	a2, 0(a0)
	lw	a1, -56(s0)
	lw	a2, -36(s0)
	addi	a0, sp, -16
	mv	sp, a0
	bne	a1, a2, .LBB0_48
# %bb.47:                               # %if.then.18
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -60(s0)
	lw	a2, -40(s0)
.LBB0_48:                               # %if.else.18
                                        #   in Loop: Header=BB0_13 Depth=1
	xor	a1, a1, a2
	seqz	a1, a1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_50
# %bb.49:                               # %if.then.19
                                        #   in Loop: Header=BB0_13 Depth=1
	sw	s1, -48(s0)
.LBB0_50:                               # %if.end.17
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -24(s0)
	lw	a1, -64(s0)
	slli	a2, a0, 2
	add	a1, a1, a2
	lw	a0, 0(a1)
	lw	a1, -68(s0)
	addi	a0, a0, -2
	sw	a0, -56(s0)
	add	a1, a1, a2
	lw	a2, 0(a1)
	lw	a1, -20(s0)
	addi	a2, a2, -1
	sw	a2, -60(s0)
	call	check
	andi	a0, a0, 1
	addi	s2, sp, -16
	mv	sp, s2
	beqz	a0, .LBB0_52
# %bb.51:                               # %if.then.20
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -60(s0)
	lw	a1, -20(s0)
	call	check
	andi	a0, a0, 1
.LBB0_52:                               # %if.else.20
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a0, 0(s2)
	lbu	a1, 0(s2)
	addi	a0, sp, -16
	mv	sp, a0
	beqz	a1, .LBB0_54
# %bb.53:                               # %if.then.21
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -56(s0)
	lw	a2, -72(s0)
	slli	a1, a1, 2
	add	a1, a2, a1
	lw	a2, -60(s0)
	lw	a1, 0(a1)
	slli	a2, a2, 2
	add	a1, a1, a2
	lw	a1, 0(a1)
	addi	a1, a1, 1
	seqz	a1, a1
.LBB0_54:                               # %if.else.21
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_59
# %bb.55:                               # %if.then.22
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -44(s0)
	addi	a0, a0, 1
	lw	a1, -64(s0)
	lw	a2, -56(s0)
	sw	a0, -44(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -44(s0)
	lw	a1, -68(s0)
	lw	a2, -60(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -56(s0)
	lw	a1, -72(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a1, -60(s0)
	lw	a0, 0(a0)
	lw	a2, -52(s0)
	slli	a1, a1, 2
	add	a0, a0, a1
	addi	a2, a2, 1
	sw	a2, 0(a0)
	lw	a1, -56(s0)
	lw	a2, -36(s0)
	addi	a0, sp, -16
	mv	sp, a0
	bne	a1, a2, .LBB0_57
# %bb.56:                               # %if.then.23
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -60(s0)
	lw	a2, -40(s0)
.LBB0_57:                               # %if.else.23
                                        #   in Loop: Header=BB0_13 Depth=1
	xor	a1, a1, a2
	seqz	a1, a1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_59
# %bb.58:                               # %if.then.24
                                        #   in Loop: Header=BB0_13 Depth=1
	sw	s1, -48(s0)
.LBB0_59:                               # %if.end.22
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -24(s0)
	lw	a1, -64(s0)
	slli	a2, a0, 2
	add	a1, a1, a2
	lw	a0, 0(a1)
	lw	a1, -68(s0)
	addi	a0, a0, -2
	sw	a0, -56(s0)
	add	a1, a1, a2
	lw	a2, 0(a1)
	lw	a1, -20(s0)
	addi	a2, a2, 1
	sw	a2, -60(s0)
	call	check
	andi	a0, a0, 1
	addi	s2, sp, -16
	mv	sp, s2
	beqz	a0, .LBB0_61
# %bb.60:                               # %if.then.25
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -60(s0)
	lw	a1, -20(s0)
	call	check
	andi	a0, a0, 1
.LBB0_61:                               # %if.else.25
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a0, 0(s2)
	lbu	a1, 0(s2)
	addi	a0, sp, -16
	mv	sp, a0
	beqz	a1, .LBB0_63
# %bb.62:                               # %if.then.26
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -56(s0)
	lw	a2, -72(s0)
	slli	a1, a1, 2
	add	a1, a2, a1
	lw	a2, -60(s0)
	lw	a1, 0(a1)
	slli	a2, a2, 2
	add	a1, a1, a2
	lw	a1, 0(a1)
	addi	a1, a1, 1
	seqz	a1, a1
.LBB0_63:                               # %if.else.26
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_68
# %bb.64:                               # %if.then.27
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -44(s0)
	addi	a0, a0, 1
	lw	a1, -64(s0)
	lw	a2, -56(s0)
	sw	a0, -44(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -44(s0)
	lw	a1, -68(s0)
	lw	a2, -60(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -56(s0)
	lw	a1, -72(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a1, -60(s0)
	lw	a0, 0(a0)
	lw	a2, -52(s0)
	slli	a1, a1, 2
	add	a0, a0, a1
	addi	a2, a2, 1
	sw	a2, 0(a0)
	lw	a1, -56(s0)
	lw	a2, -36(s0)
	addi	a0, sp, -16
	mv	sp, a0
	bne	a1, a2, .LBB0_66
# %bb.65:                               # %if.then.28
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -60(s0)
	lw	a2, -40(s0)
.LBB0_66:                               # %if.else.28
                                        #   in Loop: Header=BB0_13 Depth=1
	xor	a1, a1, a2
	seqz	a1, a1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_68
# %bb.67:                               # %if.then.29
                                        #   in Loop: Header=BB0_13 Depth=1
	sw	s1, -48(s0)
.LBB0_68:                               # %if.end.27
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -24(s0)
	lw	a1, -64(s0)
	slli	a2, a0, 2
	add	a1, a1, a2
	lw	a0, 0(a1)
	lw	a1, -68(s0)
	addi	a0, a0, 2
	sw	a0, -56(s0)
	add	a1, a1, a2
	lw	a2, 0(a1)
	lw	a1, -20(s0)
	addi	a2, a2, -1
	sw	a2, -60(s0)
	call	check
	andi	a0, a0, 1
	addi	s2, sp, -16
	mv	sp, s2
	beqz	a0, .LBB0_70
# %bb.69:                               # %if.then.30
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -60(s0)
	lw	a1, -20(s0)
	call	check
	andi	a0, a0, 1
.LBB0_70:                               # %if.else.30
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a0, 0(s2)
	lbu	a1, 0(s2)
	addi	a0, sp, -16
	mv	sp, a0
	beqz	a1, .LBB0_72
# %bb.71:                               # %if.then.31
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -56(s0)
	lw	a2, -72(s0)
	slli	a1, a1, 2
	add	a1, a2, a1
	lw	a2, -60(s0)
	lw	a1, 0(a1)
	slli	a2, a2, 2
	add	a1, a1, a2
	lw	a1, 0(a1)
	addi	a1, a1, 1
	seqz	a1, a1
.LBB0_72:                               # %if.else.31
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_77
# %bb.73:                               # %if.then.32
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -44(s0)
	addi	a0, a0, 1
	lw	a1, -64(s0)
	lw	a2, -56(s0)
	sw	a0, -44(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -44(s0)
	lw	a1, -68(s0)
	lw	a2, -60(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -56(s0)
	lw	a1, -72(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a1, -60(s0)
	lw	a0, 0(a0)
	lw	a2, -52(s0)
	slli	a1, a1, 2
	add	a0, a0, a1
	addi	a2, a2, 1
	sw	a2, 0(a0)
	lw	a1, -56(s0)
	lw	a2, -36(s0)
	addi	a0, sp, -16
	mv	sp, a0
	bne	a1, a2, .LBB0_75
# %bb.74:                               # %if.then.33
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -60(s0)
	lw	a2, -40(s0)
.LBB0_75:                               # %if.else.33
                                        #   in Loop: Header=BB0_13 Depth=1
	xor	a1, a1, a2
	seqz	a1, a1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_77
# %bb.76:                               # %if.then.34
                                        #   in Loop: Header=BB0_13 Depth=1
	sw	s1, -48(s0)
.LBB0_77:                               # %if.end.32
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -24(s0)
	lw	a1, -64(s0)
	slli	a2, a0, 2
	add	a1, a1, a2
	lw	a0, 0(a1)
	lw	a1, -68(s0)
	addi	a0, a0, 2
	sw	a0, -56(s0)
	add	a1, a1, a2
	lw	a2, 0(a1)
	lw	a1, -20(s0)
	addi	a2, a2, 1
	sw	a2, -60(s0)
	call	check
	andi	a0, a0, 1
	addi	s2, sp, -16
	mv	sp, s2
	beqz	a0, .LBB0_79
# %bb.78:                               # %if.then.35
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -60(s0)
	lw	a1, -20(s0)
	call	check
	andi	a0, a0, 1
.LBB0_79:                               # %if.else.35
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a0, 0(s2)
	lbu	a1, 0(s2)
	addi	a0, sp, -16
	mv	sp, a0
	beqz	a1, .LBB0_81
# %bb.80:                               # %if.then.36
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -56(s0)
	lw	a2, -72(s0)
	slli	a1, a1, 2
	add	a1, a2, a1
	lw	a2, -60(s0)
	lw	a1, 0(a1)
	slli	a2, a2, 2
	add	a1, a1, a2
	lw	a1, 0(a1)
	addi	a1, a1, 1
	seqz	a1, a1
.LBB0_81:                               # %if.else.36
                                        #   in Loop: Header=BB0_13 Depth=1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_86
# %bb.82:                               # %if.then.37
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -44(s0)
	addi	a0, a0, 1
	lw	a1, -64(s0)
	lw	a2, -56(s0)
	sw	a0, -44(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -44(s0)
	lw	a1, -68(s0)
	lw	a2, -60(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	sw	a2, 0(a0)
	lw	a0, -56(s0)
	lw	a1, -72(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a1, -60(s0)
	lw	a0, 0(a0)
	lw	a2, -52(s0)
	slli	a1, a1, 2
	add	a0, a0, a1
	addi	a2, a2, 1
	sw	a2, 0(a0)
	lw	a1, -56(s0)
	lw	a2, -36(s0)
	addi	a0, sp, -16
	mv	sp, a0
	bne	a1, a2, .LBB0_84
# %bb.83:                               # %if.then.38
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a1, -60(s0)
	lw	a2, -40(s0)
.LBB0_84:                               # %if.else.38
                                        #   in Loop: Header=BB0_13 Depth=1
	xor	a1, a1, a2
	seqz	a1, a1
	sb	a1, 0(a0)
	lbu	a0, 0(a0)
	beqz	a0, .LBB0_86
# %bb.85:                               # %if.then.39
                                        #   in Loop: Header=BB0_13 Depth=1
	sw	s1, -48(s0)
.LBB0_86:                               # %if.end.37
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -48(s0)
	beq	a0, s1, .LBB0_88
# %bb.87:                               # %if.end.40
                                        #   in Loop: Header=BB0_13 Depth=1
	lw	a0, -24(s0)
	addi	a0, a0, 1
	sw	a0, -24(s0)
	j	.LBB0_13
.LBB0_88:                               # %while.end.0
	lw	a0, -48(s0)
	li	a1, 1
	bne	a0, a1, .LBB0_90
# %bb.89:                               # %if.then.41
	lw	a0, -36(s0)
	lw	a1, -72(s0)
	slli	a0, a0, 2
	add	a0, a1, a0
	lw	a1, -40(s0)
	lw	a0, 0(a0)
	slli	a1, a1, 2
	add	a0, a0, a1
	lw	a0, 0(a0)
	call	toString
	call	println
	j	.LBB0_91
.LBB0_90:                               # %if.else.41
	lui	a0, %hi(string.0)
	addi	a0, a0, %lo(string.0)
	call	print
.LBB0_91:                               # %if.end.41
	li	a0, 0
	addi	sp, s0, -80
	lw	ra, 76(sp)                      # 4-byte Folded Reload
	lw	s0, 72(sp)                      # 4-byte Folded Reload
	lw	s1, 68(sp)                      # 4-byte Folded Reload
	lw	s2, 64(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 80
	ret
.Lfunc_end0:
	.size	main, .Lfunc_end0-main
	.cfi_endproc
                                        # -- End function
	.globl	check                           # -- Begin function check
	.p2align	2
	.type	check,@function
check:                                  # @check
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	a0, 12(sp)
	sw	a1, 8(sp)
	bge	a0, a1, .LBB1_2
# %bb.1:                                # %if.then.0
	lw	a0, 12(sp)
	slti	a0, a0, 0
	xori	a0, a0, 1
	j	.LBB1_3
.LBB1_2:                                # %if.else.0
	slt	a0, a0, a1
.LBB1_3:                                # %if.end.0
	sb	a0, 7(sp)
	lbu	a0, 7(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end1:
	.size	check, .Lfunc_end1-check
	.cfi_endproc
                                        # -- End function
	.type	string.0,@object                # @string.0
	.section	.rodata,"a",@progbits
	.globl	string.0
string.0:
	.asciz	"no solution!\\n"
	.size	string.0, 15

	.section	".note.GNU-stack","",@progbits
