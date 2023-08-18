declare void @print(ptr)
declare ptr @_string_concat(ptr, ptr)
declare ptr @_string_copy(ptr)
declare i32 @_string_compare(ptr, ptr)
declare ptr @_malloc_array(i32, i32)
declare void @println(ptr)
declare void @printInt(i32)
declare void @printlnInt(i32)
declare ptr @getString()
declare i32 @getInt()
declare ptr @toString(i32)
declare i32 @_string_length(ptr)
declare ptr @_string_substring(ptr, i32, i32)
declare i32 @_string_parseInt(ptr)
declare i32 @_string_ord(ptr, i32)
@a = global ptr null
@n = global i32 0
@string.0 = constant [2 x i8] c" \00"
@string.1 = constant [3 x i8] c"\\n\00"
define void @exchange(i32 %_x, i32 %_y) {
entry:
  %x.1 = alloca i32
  store i32 %_x, ptr %x.1
  %y.1 = alloca i32
  store i32 %_y, ptr %y.1
  %t.1 = alloca i32
  %_0 = load ptr, ptr @a
  %_1 = load i32, ptr %x.1
  %_2 = getelementptr i32, ptr %_0, i32 %_1
  %_3 = load i32, ptr %_2
  store i32 %_3, ptr %t.1
  %_4 = load ptr, ptr @a
  %_5 = load i32, ptr %x.1
  %_6 = getelementptr i32, ptr %_4, i32 %_5
  %_7 = load i32, ptr %_6
  %_8 = load ptr, ptr @a
  %_9 = load i32, ptr %y.1
  %_10 = getelementptr i32, ptr %_8, i32 %_9
  %_11 = load i32, ptr %_10
  store i32 %_11, ptr %_6
  %_12 = load ptr, ptr @a
  %_13 = load i32, ptr %y.1
  %_14 = getelementptr i32, ptr %_12, i32 %_13
  %_15 = load i32, ptr %_14
  %_16 = load i32, ptr %t.1
  store i32 %_16, ptr %_14
  ret void
}
define i32 @main() {
entry:
  %i.1 = alloca i32
  %_0 = load i32, ptr @n
  %_1 = call ptr @getString()
  %_2 = call i32 @_string_parseInt(ptr %_1)
  store i32 %_2, ptr @n
  %_3 = load ptr, ptr @a
  %_4 = load i32, ptr @n
  %_5 = call ptr @_malloc_array(i32 4, i32 %_4)
  store ptr %_5, ptr @a
  %_6 = load i32, ptr %i.1
  store i32 0, ptr %i.1
  br label %for.cond.0
for.cond.0:
  %_7 = load i32, ptr %i.1
  %_8 = load ptr, ptr @a
  %_9 = getelementptr i32, ptr %_8, i32 -1
  %_10 = load i32, ptr %_9
  %_11 = icmp slt i32 %_7, %_10
  br i1 %_11, label %for.body.0, label %for.end.0
for.body.0:
  %_12 = load ptr, ptr @a
  %_13 = load i32, ptr %i.1
  %_14 = getelementptr i32, ptr %_12, i32 %_13
  %_15 = load i32, ptr %_14
  %_16 = load i32, ptr %i.1
  store i32 %_16, ptr %_14
  br label %for.step.0
for.step.0:
  %_17 = load i32, ptr %i.1
  %_18 = load i32, ptr %i.1
  %_19 = add i32 %_18, 1
  store i32 %_19, ptr %i.1
  br label %for.cond.0
for.end.0:
  %_20 = call i32 @makeHeap()
  %_21 = call i32 @heapSort()
  %_22 = load i32, ptr %i.1
  store i32 0, ptr %i.1
  br label %for.cond.1
for.cond.1:
  %_23 = load i32, ptr %i.1
  %_24 = load ptr, ptr @a
  %_25 = getelementptr i32, ptr %_24, i32 -1
  %_26 = load i32, ptr %_25
  %_27 = icmp slt i32 %_23, %_26
  br i1 %_27, label %for.body.1, label %for.end.1
for.body.1:
  %_29 = load ptr, ptr @a
  %_30 = load i32, ptr %i.1
  %_31 = getelementptr i32, ptr %_29, i32 %_30
  %_32 = load i32, ptr %_31
  %_28 = call ptr @toString(i32 %_32)
  %_33 = call ptr @_string_concat(ptr %_28, ptr @string.0)
  call void @print(ptr %_33)
  br label %for.step.1
for.step.1:
  %_34 = load i32, ptr %i.1
  %_35 = load i32, ptr %i.1
  %_36 = add i32 %_35, 1
  store i32 %_36, ptr %i.1
  br label %for.cond.1
for.end.1:
  call void @print(ptr @string.1)
  ret i32 0
}
define i32 @adjustHeap(i32 %_n) {
entry:
  %n.1 = alloca i32
  store i32 %_n, ptr %n.1
  %i.1 = alloca i32
  store i32 0, ptr %i.1
  %j.1 = alloca i32
  store i32 0, ptr %j.1
  %t.1 = alloca i32
  store i32 0, ptr %t.1
  br label %while.cond.0
while.cond.0:
  %_0 = load i32, ptr %i.1
  %_1 = mul i32 %_0, 2
  %_2 = load i32, ptr %n.1
  %_3 = icmp slt i32 %_1, %_2
  br i1 %_3, label %while.body.0, label %while.end.0
while.body.0:
  %_4 = load i32, ptr %j.1
  %_5 = load i32, ptr %i.1
  %_6 = mul i32 %_5, 2
  store i32 %_6, ptr %j.1
  %_7 = load i32, ptr %i.1
  %_8 = mul i32 %_7, 2
  %_9 = add i32 %_8, 1
  %_10 = load i32, ptr %n.1
  %_11 = icmp slt i32 %_9, %_10
  %_12 = alloca i1
  br i1 %_11, label %if.then.0, label %if.else.0
if.then.0:
  %_13 = load ptr, ptr @a
  %_14 = load i32, ptr %i.1
  %_15 = mul i32 %_14, 2
  %_16 = add i32 %_15, 1
  %_17 = getelementptr i32, ptr %_13, i32 %_16
  %_18 = load i32, ptr %_17
  %_19 = load ptr, ptr @a
  %_20 = load i32, ptr %i.1
  %_21 = mul i32 %_20, 2
  %_22 = getelementptr i32, ptr %_19, i32 %_21
  %_23 = load i32, ptr %_22
  %_24 = icmp slt i32 %_18, %_23
  store i1 %_24, ptr %_12
  br label %if.end.0
if.else.0:
  store i1 %_11, ptr %_12
  br label %if.end.0
if.end.0:
  %_25 = load i1, ptr %_12
  br i1 %_25, label %if.then.1, label %if.end.1
if.then.1:
  %_26 = load i32, ptr %j.1
  %_27 = load i32, ptr %i.1
  %_28 = mul i32 %_27, 2
  %_29 = add i32 %_28, 1
  store i32 %_29, ptr %j.1
  br label %if.end.1
if.end.1:
  %_30 = load ptr, ptr @a
  %_31 = load i32, ptr %i.1
  %_32 = getelementptr i32, ptr %_30, i32 %_31
  %_33 = load i32, ptr %_32
  %_34 = load ptr, ptr @a
  %_35 = load i32, ptr %j.1
  %_36 = getelementptr i32, ptr %_34, i32 %_35
  %_37 = load i32, ptr %_36
  %_38 = icmp sgt i32 %_33, %_37
  br i1 %_38, label %if.then.2, label %if.else.2
if.then.2:
  %t.2 = alloca i32
  %_39 = load ptr, ptr @a
  %_40 = load i32, ptr %i.1
  %_41 = getelementptr i32, ptr %_39, i32 %_40
  %_42 = load i32, ptr %_41
  store i32 %_42, ptr %t.2
  %_43 = load ptr, ptr @a
  %_44 = load i32, ptr %i.1
  %_45 = getelementptr i32, ptr %_43, i32 %_44
  %_46 = load i32, ptr %_45
  %_47 = load ptr, ptr @a
  %_48 = load i32, ptr %j.1
  %_49 = getelementptr i32, ptr %_47, i32 %_48
  %_50 = load i32, ptr %_49
  store i32 %_50, ptr %_45
  %_51 = load ptr, ptr @a
  %_52 = load i32, ptr %j.1
  %_53 = getelementptr i32, ptr %_51, i32 %_52
  %_54 = load i32, ptr %_53
  %_55 = load i32, ptr %t.2
  store i32 %_55, ptr %_53
  %_56 = load i32, ptr %i.1
  %_57 = load i32, ptr %j.1
  store i32 %_57, ptr %i.1
  br label %if.end.2
if.else.2:
  br label %while.end.0
  br label %if.end.2
if.end.2:
  br label %while.cond.0
while.end.0:
  ret i32 0
}
define i32 @heapSort() {
entry:
  %t.1 = alloca i32
  %k.1 = alloca i32
  %_0 = load i32, ptr %t.1
  store i32 0, ptr %t.1
  %_1 = load i32, ptr %k.1
  store i32 0, ptr %k.1
  br label %for.cond.0
for.cond.0:
  %_2 = load i32, ptr %k.1
  %_3 = load i32, ptr @n
  %_4 = icmp slt i32 %_2, %_3
  br i1 %_4, label %for.body.0, label %for.end.0
for.body.0:
  %_5 = load i32, ptr %t.1
  %_6 = load ptr, ptr @a
  %_7 = getelementptr i32, ptr %_6, i32 0
  %_8 = load i32, ptr %_7
  store i32 %_8, ptr %t.1
  %_9 = load ptr, ptr @a
  %_10 = getelementptr i32, ptr %_9, i32 0
  %_11 = load i32, ptr %_10
  %_12 = load ptr, ptr @a
  %_13 = load i32, ptr @n
  %_14 = load i32, ptr %k.1
  %_15 = sub i32 %_13, %_14
  %_16 = sub i32 %_15, 1
  %_17 = getelementptr i32, ptr %_12, i32 %_16
  %_18 = load i32, ptr %_17
  store i32 %_18, ptr %_10
  %_19 = load ptr, ptr @a
  %_20 = load i32, ptr @n
  %_21 = load i32, ptr %k.1
  %_22 = sub i32 %_20, %_21
  %_23 = sub i32 %_22, 1
  %_24 = getelementptr i32, ptr %_19, i32 %_23
  %_25 = load i32, ptr %_24
  %_26 = load i32, ptr %t.1
  store i32 %_26, ptr %_24
  %_28 = load i32, ptr @n
  %_29 = load i32, ptr %k.1
  %_30 = sub i32 %_28, %_29
  %_31 = sub i32 %_30, 1
  %_27 = call i32 @adjustHeap(i32 %_31)
  br label %for.step.0
for.step.0:
  %_32 = load i32, ptr %k.1
  %_33 = load i32, ptr %k.1
  %_34 = add i32 %_33, 1
  store i32 %_34, ptr %k.1
  br label %for.cond.0
for.end.0:
  ret i32 0
}
define i32 @makeHeap() {
entry:
  %i.1 = alloca i32
  %t.1 = alloca i32
  %j.1 = alloca i32
  %_0 = load i32, ptr %i.1
  %_1 = load i32, ptr @n
  %_2 = sub i32 %_1, 1
  %_3 = sdiv i32 %_2, 2
  store i32 %_3, ptr %i.1
  %_4 = load i32, ptr %t.1
  store i32 0, ptr %t.1
  %_5 = load i32, ptr %j.1
  store i32 0, ptr %j.1
  br label %while.cond.0
while.cond.0:
  %_6 = load i32, ptr %i.1
  %_7 = icmp sge i32 %_6, 0
  br i1 %_7, label %while.body.0, label %while.end.0
while.body.0:
  %_8 = load i32, ptr %j.1
  %_9 = load i32, ptr %i.1
  %_10 = mul i32 %_9, 2
  store i32 %_10, ptr %j.1
  %_11 = load i32, ptr %i.1
  %_12 = mul i32 %_11, 2
  %_13 = add i32 %_12, 1
  %_14 = load i32, ptr @n
  %_15 = icmp slt i32 %_13, %_14
  %_16 = alloca i1
  br i1 %_15, label %if.then.0, label %if.else.0
if.then.0:
  %_17 = load ptr, ptr @a
  %_18 = load i32, ptr %i.1
  %_19 = mul i32 %_18, 2
  %_20 = add i32 %_19, 1
  %_21 = getelementptr i32, ptr %_17, i32 %_20
  %_22 = load i32, ptr %_21
  %_23 = load ptr, ptr @a
  %_24 = load i32, ptr %i.1
  %_25 = mul i32 %_24, 2
  %_26 = getelementptr i32, ptr %_23, i32 %_25
  %_27 = load i32, ptr %_26
  %_28 = icmp slt i32 %_22, %_27
  store i1 %_28, ptr %_16
  br label %if.end.0
if.else.0:
  store i1 %_15, ptr %_16
  br label %if.end.0
if.end.0:
  %_29 = load i1, ptr %_16
  br i1 %_29, label %if.then.1, label %if.end.1
if.then.1:
  %_30 = load i32, ptr %j.1
  %_31 = load i32, ptr %i.1
  %_32 = mul i32 %_31, 2
  %_33 = add i32 %_32, 1
  store i32 %_33, ptr %j.1
  br label %if.end.1
if.end.1:
  %_34 = load ptr, ptr @a
  %_35 = load i32, ptr %i.1
  %_36 = getelementptr i32, ptr %_34, i32 %_35
  %_37 = load i32, ptr %_36
  %_38 = load ptr, ptr @a
  %_39 = load i32, ptr %j.1
  %_40 = getelementptr i32, ptr %_38, i32 %_39
  %_41 = load i32, ptr %_40
  %_42 = icmp sgt i32 %_37, %_41
  br i1 %_42, label %if.then.2, label %if.end.2
if.then.2:
  %_43 = load i32, ptr %i.1
  %_44 = load i32, ptr %j.1
  call void @exchange(i32 %_43, i32 %_44)
  br label %if.end.2
if.end.2:
  %_45 = load i32, ptr %i.1
  %_46 = load i32, ptr %i.1
  %_47 = sub i32 %_46, 1
  store i32 %_47, ptr %i.1
  br label %while.cond.0
while.end.0:
  ret i32 0
}
