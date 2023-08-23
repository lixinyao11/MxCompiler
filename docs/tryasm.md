# 1

```c++
int a = 0;
string b;
int main() {
	a = a + 1;
	b = "11";
	return a;
}
```

```
declare ptr @_string.copy(ptr)
@a = global i32 0
@b = global ptr null
@string.0 = constant [3 x i8] c"11\00"
define i32 @main() {
entry:
  %_0 = load i32, ptr @a
  %_1 = load i32, ptr @a
  
  %_2 = add i32 %_1, 1
  store i32 %_2, ptr @a
  %_3 = load ptr, ptr @b
  %_4 = call ptr @_string.copy(ptr @string.0)
  store ptr %_4, ptr @b
  %_5 = load i32, ptr @a
  ret i32 %_5
}
```

```assembly
main:                                
        addi    sp, sp, -16
        sw      ra, 12(sp)            # callee save
        sw      s0, 8(sp)             # callee save
        sw      s1, 4(sp)             # callee save
        
        lui     s0, %hi(a)
        lw      a0, %lo(a)(s0)      # a0 = *a
        
        addi    a0, a0, 1
        sw      a0, %lo(a)(s0)     # *a = *a + 1
        
        lui     s1, %hi(b)
        lui     a0, %hi(string.0)
        addi    a0, a0, %lo(string.0)  # a0 = &string.0
        
        call    _string.copy@plt
        lw      a1, %lo(a)(s0)
        sw      a0, %lo(b)(s1)
        mv      a0, a1
        
        lw      ra, 12(sp)            # 4-byte Folded Reload
        lw      s0, 8(sp)              # 4-byte Folded Reload
        lw      s1, 4(sp)             # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
a:
        .word   0                     # 0x0

b:
        .word   0

string.0:
        .asciz  "11"
```

# 2

```c++
int a = 0;
string b;
int main() {
if (a == 1) {
b = "11";
} else {
a = 1;
}
while (a < 10) a++;
return a;
}
```

```
declare ptr @_string.copy(ptr)
@a = global i32 0
@b = global ptr null
@string.0 = constant [3 x i8] c"11\00"
define i32 @main() {
entry:
  %_0 = load i32, ptr @a
  %_1 = icmp eq i32 %_0, 1
  br i1 %_1, label %if.then.0, label %if.else.0
if.then.0:
  %_2 = load ptr, ptr @b
  %_3 = call ptr @_string.copy(ptr @string.0)
  store ptr %_3, ptr @b
  br label %if.end.0
if.else.0:
  %_4 = load i32, ptr @a
  store i32 1, ptr @a
  br label %if.end.0
if.end.0:
  br label %while.cond.0
while.cond.0:
  %_5 = load i32, ptr @a
  %_6 = icmp slt i32 %_5, 10
  br i1 %_6, label %while.body.0, label %while.end.0
while.body.0:
  %_7 = load i32, ptr @a
  %_8 = add i32 %_7, 1
  store i32 %_8, ptr @a
  br label %while.cond.0
while.end.0:
  %_9 = load i32, ptr @a
  ret i32 %_9
}
```

```assembly
main:                                   # @main
        lui     a0, %hi(a)
        lw      a2, %lo(a)(a0)
        li      a1, 1
        bne     a2, a1, .LBB0_2
        addi    sp, sp, -16
        sw      ra, 12(sp)              # 4-byte Folded Spill
        sw      s0, 8(sp)              # 4-byte Folded Spill
        lui     s0, %hi(b)
        lui     a0, %hi(string.0)
        addi    a0, a0, %lo(string.0)
        call    _string.copy@plt
        sw      a0, %lo(b)(s0)
        lw      ra, 12(sp)            # 4-byte Folded Reload
        lw      s0, 8(sp)             # 4-byte Folded Reload
        addi    sp, sp, 16
        j       .LBB0_3
.LBB0_2:                                # %if.else.0
        sw      a1, %lo(a)(a0)
.LBB0_3:                                # %if.end.0
        lui     a0, %hi(a)
        li      a1, 9
        lw      a2, %lo(a)(a0)
        blt     a1, a2, .LBB0_5
.LBB0_4:                                # %while.body.0
        lw      a2, %lo(a)(a0)
        addi    a2, a2, 1
        sw      a2, %lo(a)(a0)
        lw      a2, %lo(a)(a0)
        bge     a1, a2, .LBB0_4
.LBB0_5:                                # %while.end.0
        lui     a0, %hi(a)
        lw      a0, %lo(a)(a0)
        ret
a:
        .word   0                               # 0x0

b:
        .word   0

string.0:
        .asciz  "11"
```





label(block) :



​		.section data

​		.globl a

label(globalPtr):

 			.word(size == 4)

​			.byte(size != 4)



​		.section rodata

label(StringLiteral):

​		.asciz



# ??

.bss 类section？

.zero指令？