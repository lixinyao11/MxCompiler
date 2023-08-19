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





label(block) :



​		.section data

​		.globl a

label(globalPtr):

 			.word



​		.section rodata

label(StringLiteral):

​		.asciz

​		.size <label> 3



# ??

.bss 类section？

.zero指令？