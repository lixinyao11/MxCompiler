#  roadmap

## 写g4

可以参考Yx.g4

### statement

{}中的内容

while (expression) {statement} //(不能是所有statement，如funcStmt)

statement: ... #

| ... #

| ... #

### expression

x operator x

按优先级排序

#binaryExpr : ++, +, *, % ...

#binaryAdd #binaryMultiply

## debug g4

`+++++c`等极端例子

利用插件

## g4->ast

### ASTNodes

### ASTBuilder

### SymbolCollector/globalScope/Scope

### SemanticChecker

## ast->ir


# todo-list
1. array.size()的实现

# mark
1. 重新编译g4！！
2. arrayExpr取下标操作可能出现嵌套，如二维数组a，a[1][2\]
3. 如果return语句缺失且非void要报错，但不考虑return语句能否到达，也不考虑是否每个分支都return

