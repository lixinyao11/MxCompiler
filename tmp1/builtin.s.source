	.text
	.attribute	4, 16
	.attribute	5, "rv32i2p1"
	.file	"builtin.c"
	.globl	print                           # -- Begin function print
	.p2align	1
	.type	print,@function
print:                                  # @print
# %bb.0:
	lui	a1, %hi(.L.str)
	addi	a1, a1, %lo(.L.str)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end0:
	.size	print, .Lfunc_end0-print
                                        # -- End function
	.globl	println                         # -- Begin function println
	.p2align	1
	.type	println,@function
println:                                # @println
# %bb.0:
	lui	a1, %hi(.L.str.1)
	addi	a1, a1, %lo(.L.str.1)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end1:
	.size	println, .Lfunc_end1-println
                                        # -- End function
	.globl	printInt                        # -- Begin function printInt
	.p2align	1
	.type	printInt,@function
printInt:                               # @printInt
# %bb.0:
	lui	a1, %hi(.L.str.2)
	addi	a1, a1, %lo(.L.str.2)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end2:
	.size	printInt, .Lfunc_end2-printInt
                                        # -- End function
	.globl	printlnInt                      # -- Begin function printlnInt
	.p2align	1
	.type	printlnInt,@function
printlnInt:                             # @printlnInt
# %bb.0:
	lui	a1, %hi(.L.str.3)
	addi	a1, a1, %lo(.L.str.3)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end3:
	.size	printlnInt, .Lfunc_end3-printlnInt
                                        # -- End function
	.globl	_malloc_array                   # -- Begin function _malloc_array
	.p2align	1
	.type	_malloc_array,@function
_malloc_array:                          # @_malloc_array
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	mv	s0, a1
	mul	a0, a1, a0
	addi	a0, a0, 4
	call	malloc
	addi	a1, a0, 4
	sw	s0, 0(a0)
	mv	a0, a1
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end4:
	.size	_malloc_array, .Lfunc_end4-_malloc_array
                                        # -- End function
	.globl	_malloc                         # -- Begin function _malloc
	.p2align	1
	.type	_malloc,@function
_malloc:                                # @_malloc
# %bb.0:
	tail	malloc
.Lfunc_end5:
	.size	_malloc, .Lfunc_end5-_malloc
                                        # -- End function
	.globl	getString                       # -- Begin function getString
	.p2align	1
	.type	getString,@function
getString:                              # @getString
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	li	a0, 102
	call	malloc
	mv	s0, a0
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	mv	a1, s0
	call	scanf
	mv	a0, s0
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end6:
	.size	getString, .Lfunc_end6-getString
                                        # -- End function
	.globl	getInt                          # -- Begin function getInt
	.p2align	1
	.type	getInt,@function
getInt:                                 # @getInt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	lui	a0, %hi(.L.str.2)
	addi	a0, a0, %lo(.L.str.2)
	addi	a1, sp, 8
	call	scanf
	lw	a0, 8(sp)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end7:
	.size	getInt, .Lfunc_end7-getInt
                                        # -- End function
	.globl	toString                        # -- Begin function toString
	.p2align	1
	.type	toString,@function
toString:                               # @toString
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	sw	s1, 4(sp)                       # 4-byte Folded Spill
	mv	s0, a0
	li	a0, 12
	call	malloc
	mv	s1, a0
	lui	a0, %hi(.L.str.2)
	addi	a1, a0, %lo(.L.str.2)
	mv	a0, s1
	mv	a2, s0
	call	sprintf
	mv	a0, s1
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	lw	s1, 4(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end8:
	.size	toString, .Lfunc_end8-toString
                                        # -- End function
	.globl	_string.length                  # -- Begin function _string.length
	.p2align	1
	.type	_string.length,@function
_string.length:                         # @_string.length
# %bb.0:
	li	a1, 0
.LBB9_1:                                # =>This Inner Loop Header: Depth=1
	add	a2, a0, a1
	lbu	a2, 0(a2)
	addi	a1, a1, 1
	bnez	a2, .LBB9_1
# %bb.2:
	addi	a0, a1, -1
	ret
.Lfunc_end9:
	.size	_string.length, .Lfunc_end9-_string.length
                                        # -- End function
	.globl	_string.substring               # -- Begin function _string.substring
	.p2align	1
	.type	_string.substring,@function
_string.substring:                      # @_string.substring
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	sw	s1, 4(sp)                       # 4-byte Folded Spill
	sw	s2, 0(sp)                       # 4-byte Folded Spill
	mv	s0, a1
	mv	s2, a0
	sub	s1, a2, a1
	addi	a0, s1, 1
	call	malloc
	blez	s1, .LBB10_3
# %bb.1:                                # %.preheader
	add	s0, s0, s2
	mv	a1, a0
	mv	a2, s1
.LBB10_2:                               # =>This Inner Loop Header: Depth=1
	lbu	a3, 0(s0)
	sb	a3, 0(a1)
	addi	a2, a2, -1
	addi	a1, a1, 1
	addi	s0, s0, 1
	bnez	a2, .LBB10_2
.LBB10_3:
	add	s1, s1, a0
	sb	zero, 0(s1)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	lw	s1, 4(sp)                       # 4-byte Folded Reload
	lw	s2, 0(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end10:
	.size	_string.substring, .Lfunc_end10-_string.substring
                                        # -- End function
	.globl	_string.parseInt                # -- Begin function _string.parseInt
	.p2align	1
	.type	_string.parseInt,@function
_string.parseInt:                       # @_string.parseInt
# %bb.0:
	lbu	a1, 0(a0)
	addi	a2, a1, -45
	seqz	a3, a2
	add	a3, a3, a0
	lbu	a2, 0(a3)
	beqz	a2, .LBB11_6
# %bb.1:                                # %.preheader
	li	a0, 0
	addi	a3, a3, 1
	li	a4, 10
.LBB11_2:                               # =>This Inner Loop Header: Depth=1
	andi	a5, a2, 255
	lbu	a2, 0(a3)
	mul	a0, a0, a4
	add	a0, a0, a5
	addi	a0, a0, -48
	addi	a3, a3, 1
	bnez	a2, .LBB11_2
# %bb.3:
	li	a2, 45
	bne	a1, a2, .LBB11_5
.LBB11_4:
	neg	a0, a0
.LBB11_5:
	ret
.LBB11_6:
	li	a0, 0
	li	a2, 45
	beq	a1, a2, .LBB11_4
	j	.LBB11_5
.Lfunc_end11:
	.size	_string.parseInt, .Lfunc_end11-_string.parseInt
                                        # -- End function
	.globl	_string.ord                     # -- Begin function _string.ord
	.p2align	1
	.type	_string.ord,@function
_string.ord:                            # @_string.ord
# %bb.0:
	add	a0, a0, a1
	lbu	a0, 0(a0)
	ret
.Lfunc_end12:
	.size	_string.ord, .Lfunc_end12-_string.ord
                                        # -- End function
	.globl	_string.compare                 # -- Begin function _string.compare
	.p2align	1
	.type	_string.compare,@function
_string.compare:                        # @_string.compare
# %bb.0:
	lbu	a2, 0(a0)
	beqz	a2, .LBB13_5
# %bb.1:                                # %.preheader
	li	a3, 0
	addi	a0, a0, 1
.LBB13_2:                               # =>This Inner Loop Header: Depth=1
	add	a4, a1, a3
	lbu	a4, 0(a4)
	beqz	a4, .LBB13_6
# %bb.3:                                #   in Loop: Header=BB13_2 Depth=1
	andi	a5, a2, 255
	bne	a5, a4, .LBB13_8
# %bb.4:                                #   in Loop: Header=BB13_2 Depth=1
	add	a2, a0, a3
	lbu	a2, 0(a2)
	addi	a4, a3, 1
	mv	a3, a4
	bnez	a2, .LBB13_2
	j	.LBB13_7
.LBB13_5:
	li	a4, 0
	j	.LBB13_7
.LBB13_6:
	mv	a4, a3
.LBB13_7:
	add	a1, a1, a4
	lbu	a4, 0(a1)
.LBB13_8:
	andi	a0, a2, 255
	sub	a0, a0, a4
	ret
.Lfunc_end13:
	.size	_string.compare, .Lfunc_end13-_string.compare
                                        # -- End function
	.globl	_string.concat                  # -- Begin function _string.concat
	.p2align	1
	.type	_string.concat,@function
_string.concat:                         # @_string.concat
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	sw	s1, 20(sp)                      # 4-byte Folded Spill
	sw	s2, 16(sp)                      # 4-byte Folded Spill
	sw	s3, 12(sp)                      # 4-byte Folded Spill
	sw	s4, 8(sp)                       # 4-byte Folded Spill
	mv	s2, a1
	mv	s4, a0
	li	a0, 0
	li	a1, -1
.LBB14_1:                               # =>This Inner Loop Header: Depth=1
	mv	s3, a0
	add	a0, a0, s4
	lbu	a2, 0(a0)
	mv	s0, a1
	addi	a0, s3, 1
	addi	a1, a1, 1
	bnez	a2, .LBB14_1
# %bb.2:                                # %.preheader3
	li	s1, 1
	mv	a1, s2
.LBB14_3:                               # =>This Inner Loop Header: Depth=1
	lbu	a2, 0(a1)
	addi	s1, s1, -1
	addi	s0, s0, 1
	addi	a1, a1, 1
	bnez	a2, .LBB14_3
# %bb.4:
	sub	a0, a0, s1
	call	malloc
	beqz	s3, .LBB14_7
# %bb.5:
	mv	a1, a0
	mv	a2, s3
.LBB14_6:                               # =>This Inner Loop Header: Depth=1
	lbu	a3, 0(s4)
	sb	a3, 0(a1)
	addi	a2, a2, -1
	addi	a1, a1, 1
	addi	s4, s4, 1
	bnez	a2, .LBB14_6
.LBB14_7:
	beqz	s1, .LBB14_10
# %bb.8:                                # %.preheader
	neg	a1, s1
	add	s3, s3, a0
.LBB14_9:                               # =>This Inner Loop Header: Depth=1
	lbu	a2, 0(s2)
	sb	a2, 0(s3)
	addi	a1, a1, -1
	addi	s3, s3, 1
	addi	s2, s2, 1
	bnez	a1, .LBB14_9
.LBB14_10:
	add	s0, s0, a0
	sb	zero, 0(s0)
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	lw	s1, 20(sp)                      # 4-byte Folded Reload
	lw	s2, 16(sp)                      # 4-byte Folded Reload
	lw	s3, 12(sp)                      # 4-byte Folded Reload
	lw	s4, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end14:
	.size	_string.concat, .Lfunc_end14-_string.concat
                                        # -- End function
	.globl	_string.copy                    # -- Begin function _string.copy
	.p2align	1
	.type	_string.copy,@function
_string.copy:                           # @_string.copy
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	sw	s1, 4(sp)                       # 4-byte Folded Spill
	mv	s0, a0
	li	s1, -1
.LBB15_1:                               # =>This Inner Loop Header: Depth=1
	add	a0, s0, s1
	lbu	a0, 1(a0)
	addi	s1, s1, 1
	bnez	a0, .LBB15_1
# %bb.2:
	addi	a0, s1, 1
	call	malloc
	beqz	s1, .LBB15_5
# %bb.3:
	mv	a1, a0
	mv	a2, s1
.LBB15_4:                               # =>This Inner Loop Header: Depth=1
	lbu	a3, 0(s0)
	sb	a3, 0(a1)
	addi	a2, a2, -1
	addi	a1, a1, 1
	addi	s0, s0, 1
	bnez	a2, .LBB15_4
.LBB15_5:
	add	s1, s1, a0
	sb	zero, 0(s1)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	lw	s1, 4(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end15:
	.size	_string.copy, .Lfunc_end15-_string.copy
                                        # -- End function
	.type	.L.str,@object                  # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"%s"
	.size	.L.str, 3

	.type	.L.str.1,@object                # @.str.1
.L.str.1:
	.asciz	"%s\n"
	.size	.L.str.1, 4

	.type	.L.str.2,@object                # @.str.2
.L.str.2:
	.asciz	"%d"
	.size	.L.str.2, 3

	.type	.L.str.3,@object                # @.str.3
.L.str.3:
	.asciz	"%d\n"
	.size	.L.str.3, 4

	.ident	"Ubuntu clang version 17.0.0 (++20230815093759+2f0fb9346d0b-1~exp1~20230815093910.22)"
	.section	".note.GNU-stack","",@progbits
