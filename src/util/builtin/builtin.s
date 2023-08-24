	.text
	.attribute	4, 16
	.attribute	5, "rv32i2p1_m2p0_a2p1_c2p0"
	.file	"builtin.c"
	.globl	print                           # -- Begin function print
	.p2align	1
	.type	print,@function
print:                                  # @print
# %bb.0:
	mv	a1, a0
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	tail	printf
.Lfunc_end0:
	.size	print, .Lfunc_end0-print
                                        # -- End function
	.globl	println                         # -- Begin function println
	.p2align	1
	.type	println,@function
println:                                # @println
# %bb.0:
	mv	a1, a0
	lui	a0, %hi(.L.str.1)
	addi	a0, a0, %lo(.L.str.1)
	tail	printf
.Lfunc_end1:
	.size	println, .Lfunc_end1-println
                                        # -- End function
	.globl	printInt                        # -- Begin function printInt
	.p2align	1
	.type	printInt,@function
printInt:                               # @printInt
# %bb.0:
	mv	a1, a0
	lui	a0, %hi(.L.str.2)
	addi	a0, a0, %lo(.L.str.2)
	tail	printf
.Lfunc_end2:
	.size	printInt, .Lfunc_end2-printInt
                                        # -- End function
	.globl	printlnInt                      # -- Begin function printlnInt
	.p2align	1
	.type	printlnInt,@function
printlnInt:                             # @printlnInt
# %bb.0:
	mv	a1, a0
	lui	a0, %hi(.L.str.3)
	addi	a0, a0, %lo(.L.str.3)
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
	sw	a1, 8(sp)                       # 4-byte Folded Spill
	mul	a0, a1, a0
	addi	a0, a0, 4
	call	malloc
	lw	a1, 8(sp)                       # 4-byte Folded Reload
	sw	a1, 0(a0)
	addi	a0, a0, 4
	lw	ra, 12(sp)                      # 4-byte Folded Reload
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
	li	a0, 102
	call	malloc
	mv	a1, a0
	sw	a1, 8(sp)                       # 4-byte Folded Spill
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	call	scanf
                                        # kill: def $x11 killed $x10
	lw	a0, 8(sp)                       # 4-byte Folded Reload
	lw	ra, 12(sp)                      # 4-byte Folded Reload
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
	sw	a0, 4(sp)                       # 4-byte Folded Spill
	li	a0, 12
	call	malloc
	lw	a2, 4(sp)                       # 4-byte Folded Reload
	sw	a0, 8(sp)                       # 4-byte Folded Spill
	lui	a1, %hi(.L.str.2)
	addi	a1, a1, %lo(.L.str.2)
	call	sprintf
                                        # kill: def $x11 killed $x10
	lw	a0, 8(sp)                       # 4-byte Folded Reload
	lw	ra, 12(sp)                      # 4-byte Folded Reload
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
	addi	sp, sp, -16
	sw	a0, 8(sp)                       # 4-byte Folded Spill
	li	a0, 0
	sw	a0, 12(sp)                      # 4-byte Folded Spill
	j	.LBB9_1
.LBB9_1:                                # =>This Inner Loop Header: Depth=1
	lw	a1, 12(sp)                      # 4-byte Folded Reload
	lw	a0, 8(sp)                       # 4-byte Folded Reload
	sw	a1, 4(sp)                       # 4-byte Folded Spill
	add	a0, a0, a1
	lbu	a0, 0(a0)
	addi	a1, a1, 1
	sw	a1, 12(sp)                      # 4-byte Folded Spill
	bnez	a0, .LBB9_1
	j	.LBB9_2
.LBB9_2:
	lw	a0, 4(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end9:
	.size	_string.length, .Lfunc_end9-_string.length
                                        # -- End function
	.globl	_string.substring               # -- Begin function _string.substring
	.p2align	1
	.type	_string.substring,@function
_string.substring:                      # @_string.substring
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	a1, 8(sp)                       # 4-byte Folded Spill
	sw	a0, 12(sp)                      # 4-byte Folded Spill
	sub	a0, a2, a1
	sw	a0, 16(sp)                      # 4-byte Folded Spill
	addi	a0, a0, 1
	call	malloc
	lw	a1, 16(sp)                      # 4-byte Folded Reload
	sw	a0, 20(sp)                      # 4-byte Folded Spill
	li	a0, 0
	mv	a2, a0
	sw	a2, 24(sp)                      # 4-byte Folded Spill
	blt	a0, a1, .LBB10_2
	j	.LBB10_1
.LBB10_1:
	lw	a0, 20(sp)                      # 4-byte Folded Reload
	lw	a1, 16(sp)                      # 4-byte Folded Reload
	add	a2, a0, a1
	li	a1, 0
	sb	a1, 0(a2)
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.LBB10_2:                               # =>This Inner Loop Header: Depth=1
	lw	a1, 16(sp)                      # 4-byte Folded Reload
	lw	a0, 24(sp)                      # 4-byte Folded Reload
	lw	a3, 20(sp)                      # 4-byte Folded Reload
	lw	a2, 12(sp)                      # 4-byte Folded Reload
	lw	a4, 8(sp)                       # 4-byte Folded Reload
	add	a4, a4, a0
	add	a2, a2, a4
	lbu	a2, 0(a2)
	add	a3, a3, a0
	sb	a2, 0(a3)
	addi	a0, a0, 1
	mv	a2, a0
	sw	a2, 24(sp)                      # 4-byte Folded Spill
	beq	a0, a1, .LBB10_1
	j	.LBB10_2
.Lfunc_end10:
	.size	_string.substring, .Lfunc_end10-_string.substring
                                        # -- End function
	.globl	_string.parseInt                # -- Begin function _string.parseInt
	.p2align	1
	.type	_string.parseInt,@function
_string.parseInt:                       # @_string.parseInt
# %bb.0:
	addi	sp, sp, -32
	sw	a0, 12(sp)                      # 4-byte Folded Spill
	lbu	a1, 0(a0)
	addi	a1, a1, -45
	seqz	a2, a1
	mv	a1, a2
	sw	a1, 16(sp)                      # 4-byte Folded Spill
	add	a0, a0, a2
	lbu	a0, 0(a0)
	li	a1, 0
	mv	a3, a0
	sw	a3, 20(sp)                      # 4-byte Folded Spill
	sw	a2, 24(sp)                      # 4-byte Folded Spill
	sw	a1, 28(sp)                      # 4-byte Folded Spill
	beqz	a0, .LBB11_2
	j	.LBB11_1
.LBB11_1:                               # =>This Inner Loop Header: Depth=1
	lw	a1, 28(sp)                      # 4-byte Folded Reload
	lw	a2, 24(sp)                      # 4-byte Folded Reload
	lw	a3, 20(sp)                      # 4-byte Folded Reload
	lw	a0, 12(sp)                      # 4-byte Folded Reload
	andi	a3, a3, 255
	li	a4, 10
	mul	a1, a1, a4
	add	a1, a1, a3
	addi	a1, a1, -48
	addi	a2, a2, 1
	add	a0, a0, a2
	lbu	a0, 0(a0)
	mv	a3, a0
	sw	a3, 20(sp)                      # 4-byte Folded Spill
	sw	a2, 24(sp)                      # 4-byte Folded Spill
	sw	a1, 28(sp)                      # 4-byte Folded Spill
	bnez	a0, .LBB11_1
	j	.LBB11_2
.LBB11_2:
	lw	a0, 16(sp)                      # 4-byte Folded Reload
	lw	a2, 28(sp)                      # 4-byte Folded Reload
	sw	a2, 4(sp)                       # 4-byte Folded Spill
	li	a1, 0
	sub	a1, a1, a2
	andi	a0, a0, 1
	sw	a1, 8(sp)                       # 4-byte Folded Spill
	bnez	a0, .LBB11_4
# %bb.3:
	lw	a0, 4(sp)                       # 4-byte Folded Reload
	sw	a0, 8(sp)                       # 4-byte Folded Spill
.LBB11_4:
	lw	a0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
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
	addi	sp, sp, -48
	sw	a1, 24(sp)                      # 4-byte Folded Spill
	sw	a0, 28(sp)                      # 4-byte Folded Spill
	lbu	a0, 0(a0)
	li	a2, 0
	mv	a1, a2
	mv	a3, a0
	sw	a3, 32(sp)                      # 4-byte Folded Spill
	mv	a3, a2
	sw	a3, 36(sp)                      # 4-byte Folded Spill
	sw	a2, 40(sp)                      # 4-byte Folded Spill
	sw	a1, 44(sp)                      # 4-byte Folded Spill
	beqz	a0, .LBB13_4
	j	.LBB13_1
.LBB13_1:                               # =>This Inner Loop Header: Depth=1
	lw	a0, 24(sp)                      # 4-byte Folded Reload
	lw	a1, 32(sp)                      # 4-byte Folded Reload
	lw	a2, 36(sp)                      # 4-byte Folded Reload
	sw	a2, 12(sp)                      # 4-byte Folded Spill
	sw	a1, 16(sp)                      # 4-byte Folded Spill
	add	a0, a0, a2
	lbu	a0, 0(a0)
	sw	a0, 20(sp)                      # 4-byte Folded Spill
	sw	a2, 40(sp)                      # 4-byte Folded Spill
	sw	a1, 44(sp)                      # 4-byte Folded Spill
	beqz	a0, .LBB13_4
	j	.LBB13_2
.LBB13_2:                               #   in Loop: Header=BB13_1 Depth=1
	lw	a2, 20(sp)                      # 4-byte Folded Reload
	lw	a3, 16(sp)                      # 4-byte Folded Reload
	andi	a1, a2, 255
	andi	a0, a3, 255
	sw	a3, 4(sp)                       # 4-byte Folded Spill
	sw	a2, 8(sp)                       # 4-byte Folded Spill
	bne	a0, a1, .LBB13_5
	j	.LBB13_3
.LBB13_3:                               #   in Loop: Header=BB13_1 Depth=1
	lw	a0, 28(sp)                      # 4-byte Folded Reload
	lw	a1, 12(sp)                      # 4-byte Folded Reload
	addi	a2, a1, 1
	add	a0, a0, a2
	lbu	a0, 0(a0)
	li	a1, 0
	mv	a3, a0
	sw	a3, 32(sp)                      # 4-byte Folded Spill
	mv	a3, a2
	sw	a3, 36(sp)                      # 4-byte Folded Spill
	sw	a2, 40(sp)                      # 4-byte Folded Spill
	sw	a1, 44(sp)                      # 4-byte Folded Spill
	bnez	a0, .LBB13_1
	j	.LBB13_4
.LBB13_4:
	lw	a0, 24(sp)                      # 4-byte Folded Reload
	lw	a2, 40(sp)                      # 4-byte Folded Reload
	lw	a1, 44(sp)                      # 4-byte Folded Reload
	add	a0, a0, a2
	lbu	a0, 0(a0)
	sw	a1, 4(sp)                       # 4-byte Folded Spill
	sw	a0, 8(sp)                       # 4-byte Folded Spill
	j	.LBB13_5
.LBB13_5:
	lw	a0, 4(sp)                       # 4-byte Folded Reload
	lw	a1, 8(sp)                       # 4-byte Folded Reload
	andi	a0, a0, 255
	andi	a1, a1, 255
	sub	a0, a0, a1
	addi	sp, sp, 48
	ret
.Lfunc_end13:
	.size	_string.compare, .Lfunc_end13-_string.compare
                                        # -- End function
	.globl	_string.concat                  # -- Begin function _string.concat
	.p2align	1
	.type	_string.concat,@function
_string.concat:                         # @_string.concat
# %bb.0:
	addi	sp, sp, -48
	sw	ra, 44(sp)                      # 4-byte Folded Spill
	sw	a1, 32(sp)                      # 4-byte Folded Spill
	sw	a0, 36(sp)                      # 4-byte Folded Spill
	li	a0, 0
	sw	a0, 40(sp)                      # 4-byte Folded Spill
	j	.LBB14_1
.LBB14_1:                               # =>This Inner Loop Header: Depth=1
	lw	a1, 40(sp)                      # 4-byte Folded Reload
	lw	a0, 36(sp)                      # 4-byte Folded Reload
	sw	a1, 24(sp)                      # 4-byte Folded Spill
	add	a0, a0, a1
	lbu	a0, 0(a0)
	addi	a2, a1, 1
	li	a1, 0
	sw	a2, 40(sp)                      # 4-byte Folded Spill
	sw	a1, 28(sp)                      # 4-byte Folded Spill
	bnez	a0, .LBB14_1
	j	.LBB14_2
.LBB14_2:                               # =>This Inner Loop Header: Depth=1
	lw	a1, 28(sp)                      # 4-byte Folded Reload
	lw	a0, 32(sp)                      # 4-byte Folded Reload
	sw	a1, 20(sp)                      # 4-byte Folded Spill
	add	a0, a0, a1
	lbu	a0, 0(a0)
	addi	a1, a1, 1
	sw	a1, 28(sp)                      # 4-byte Folded Spill
	bnez	a0, .LBB14_2
	j	.LBB14_3
.LBB14_3:
	lw	a1, 24(sp)                      # 4-byte Folded Reload
	lw	a0, 20(sp)                      # 4-byte Folded Reload
	add	a0, a0, a1
	sw	a0, 8(sp)                       # 4-byte Folded Spill
	addi	a0, a0, 1
	call	malloc
	mv	a1, a0
	lw	a0, 24(sp)                      # 4-byte Folded Reload
	sw	a1, 12(sp)                      # 4-byte Folded Spill
	li	a1, 0
	sw	a1, 16(sp)                      # 4-byte Folded Spill
	bnez	a0, .LBB14_5
	j	.LBB14_4
.LBB14_4:
	lw	a0, 20(sp)                      # 4-byte Folded Reload
	li	a1, 0
	sw	a1, 4(sp)                       # 4-byte Folded Spill
	beqz	a0, .LBB14_6
	j	.LBB14_7
.LBB14_5:                               # =>This Inner Loop Header: Depth=1
	lw	a1, 24(sp)                      # 4-byte Folded Reload
	lw	a0, 16(sp)                      # 4-byte Folded Reload
	lw	a3, 12(sp)                      # 4-byte Folded Reload
	lw	a2, 36(sp)                      # 4-byte Folded Reload
	add	a2, a2, a0
	lbu	a2, 0(a2)
	add	a3, a3, a0
	sb	a2, 0(a3)
	addi	a0, a0, 1
	mv	a2, a0
	sw	a2, 16(sp)                      # 4-byte Folded Spill
	beq	a0, a1, .LBB14_4
	j	.LBB14_5
.LBB14_6:
	lw	a0, 12(sp)                      # 4-byte Folded Reload
	lw	a1, 8(sp)                       # 4-byte Folded Reload
	add	a2, a0, a1
	li	a1, 0
	sb	a1, 0(a2)
	lw	ra, 44(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 48
	ret
.LBB14_7:                               # =>This Inner Loop Header: Depth=1
	lw	a1, 20(sp)                      # 4-byte Folded Reload
	lw	a0, 4(sp)                       # 4-byte Folded Reload
	lw	a3, 12(sp)                      # 4-byte Folded Reload
	lw	a4, 24(sp)                      # 4-byte Folded Reload
	lw	a2, 32(sp)                      # 4-byte Folded Reload
	add	a2, a2, a0
	lbu	a2, 0(a2)
	add	a4, a4, a0
	add	a3, a3, a4
	sb	a2, 0(a3)
	addi	a0, a0, 1
	mv	a2, a0
	sw	a2, 4(sp)                       # 4-byte Folded Spill
	beq	a0, a1, .LBB14_6
	j	.LBB14_7
.Lfunc_end14:
	.size	_string.concat, .Lfunc_end14-_string.concat
                                        # -- End function
	.globl	_string.copy                    # -- Begin function _string.copy
	.p2align	1
	.type	_string.copy,@function
_string.copy:                           # @_string.copy
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	a0, 20(sp)                      # 4-byte Folded Spill
	li	a0, 0
	sw	a0, 24(sp)                      # 4-byte Folded Spill
	j	.LBB15_1
.LBB15_1:                               # =>This Inner Loop Header: Depth=1
	lw	a1, 24(sp)                      # 4-byte Folded Reload
	lw	a0, 20(sp)                      # 4-byte Folded Reload
	sw	a1, 12(sp)                      # 4-byte Folded Spill
	add	a0, a0, a1
	lbu	a0, 0(a0)
	addi	a1, a1, 1
	sw	a1, 16(sp)                      # 4-byte Folded Spill
	sw	a1, 24(sp)                      # 4-byte Folded Spill
	bnez	a0, .LBB15_1
	j	.LBB15_2
.LBB15_2:
	lw	a0, 16(sp)                      # 4-byte Folded Reload
	call	malloc
	mv	a1, a0
	lw	a0, 12(sp)                      # 4-byte Folded Reload
	sw	a1, 4(sp)                       # 4-byte Folded Spill
	li	a1, 0
	sw	a1, 8(sp)                       # 4-byte Folded Spill
	bnez	a0, .LBB15_4
	j	.LBB15_3
.LBB15_3:
	lw	a0, 4(sp)                       # 4-byte Folded Reload
	lw	a1, 12(sp)                      # 4-byte Folded Reload
	add	a2, a0, a1
	li	a1, 0
	sb	a1, 0(a2)
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.LBB15_4:                               # =>This Inner Loop Header: Depth=1
	lw	a1, 12(sp)                      # 4-byte Folded Reload
	lw	a0, 8(sp)                       # 4-byte Folded Reload
	lw	a3, 4(sp)                       # 4-byte Folded Reload
	lw	a2, 20(sp)                      # 4-byte Folded Reload
	add	a2, a2, a0
	lbu	a2, 0(a2)
	add	a3, a3, a0
	sb	a2, 0(a3)
	addi	a0, a0, 1
	mv	a2, a0
	sw	a2, 8(sp)                       # 4-byte Folded Spill
	beq	a0, a1, .LBB15_3
	j	.LBB15_4
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
	.addrsig
