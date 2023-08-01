# CST

## lexer

`CharStream` --> `TokenStream`

## parser

得到CST

叶子节点：`TerminalNode` 记录token的具体值（开始和结束字符的索引值）

非叶节点：`RuleNode`，根据语法规则生成不同子类`Context`记录儿子等信息



# AST

## Build AST

### ASTNode

#### ProgramNode

##### FunDefNode
1. ReturnType --> type | void

2. Identifier --> String

3. ParaListNode --> array: {type, string}

4. Block --> array: StmtNodes

##### ClassDefNode
1. Identifier

2. ClassBuildNode (**only one**)
   1. Identifier
   2. Block --> array: StmtNodes

3. varDefNode

4. FunDefNode

##### VarDefNode

1. type

2. array: Identifier ExprNode

#### StmtNode
**(EmptyStmt没有node，需要处理一下，作为null的StmtNode)**

##### blockStmtNode

1. array: StmtNode

##### varDefStmtNode

1. varDefNode

##### IfStmt
1. ExprNode: cond

2. StmtNode: thenStmt

3. StmtNode: elseStmt // can be null

##### forStmt
1. ExprNode: init // can be null

2. ExprNode: cond // can be null

3. ExprNode: step // can be null

4. StmtNode: body

##### whileStmt
1. ExprNode: cond

2. StmtNode: body

##### ReturnStmt
1. ExprNode: retExpr // can be null

##### BreakStmt
##### continueStmt
##### ExprStmt
1. ExprNode: expr

#### ExprNode

**(parenExpr没有node，处理一下，直接跳到内部的expr)**

##### newExpr

1. Type (both newArray and newVar)

##### callExpr

1. ExprNode: func
2. array: ExprNode

##### arrayExpr

1. ExprNode: array
2. ExprNode: index

##### memberExpr

1. ExprNode: obj
2. String: member

##### unaryExpr

1. String: op
2. ExprNode: expr

##### preSelfExpr

1. String: op
2. ExprNode: expr

##### binaryExpr

1. String: op
2. ExprNode: lhs
3. ExprNode: rhs

##### conditionalExpr

1. ExprNode: cond
2. ExprNode: thenExpr
3. ExprNode: elseExpr

##### assignExpr

1. String: op
2. ExprNode: lhs
3. ExprNode: rhs

##### atomExpr

1. isThis, isTrue, isFalse, isInt, isString, isNull, isIdentifier
2. String


### Type

支持：int/string/bool/自定义class类型的**变量or数组**

若为自定义class： 保存class member？

```
type : typeName ('['']')* ;
typeName : baseType | Identifier;
baseType : Bool | Int | String；
```

### ASTBuilder: extends BaseVisitor

`visit(pareTreeRoot)` returns an `ASTNode` (implemented as `RootNode`)
this turns the CST into an AST

need to override the visit functions in BaseVisitor

## visit AST and check

### ASTVisitor

遍历所有节点，对每个节点可能出现的错误判断

### Decl

#### FuncDecl

保存一个函数的名字，返回类型(Type)，参数列表的类型(Type)

`BuiltinScope`中存内建函数

#### TypeDecl

保存一个String(name)，及其成员变量的类型(Type)和名字(String)，成员函数的声明(FuncDecl)

`BuiltinScope`中存`int`，`bool`，`string`(及其内建函数)

### scope

只保存varDefs

1. 在一段语句中，由 `{` 和 `}` 组成的块会引进一个新的作用域
2. 用户定义函数入口会引入一个新的作用域
3. 用户定义类的入口会引入一个新的作用域，该作用域里声明的所有成员，作用域为整个类
4. 全局变量和局部变量不支持前向引用，作用域为声明开始的位置直到最近的一个块的结束位置
5. 函数和类的声明都应该在顶层，作用域为全局，支持前向引用（Forward Reference）
6. 不同作用域的时候，内层作用域可以遮蔽外层作用域的名字
7. 函数名和变量名不允许重复，但是变量名和类名可以重复
8. 在同一个作用域内，变量，函数，和类，都分别不能同名（即变量不能和变量同名，其余同理），如果重名视为语法错误。
9. 在同一个作用域内，变量和函数可以重名，但是类不可以和变量、函数重名。

**注意**：诸如 `for` 等表达式没有大括号也会引入一个新的作用域

#### Scope

只保存`varDefs`(`map<string, Type>`)

#### BuiltinScope extends Scope

保存`funcdcls`, `classdcls`和`vardefs`

添加内置类型和内建函数

#### GlobalScope extends BuiltinScope

`SymbolCollector`向其中添加`funcdcl`和`classdcl`

#### ClassScope extends Scope

保存varDefs, funcdcls(SymbolCollector)

### symbolCollector

先visit一遍AST，解决前向引用的问题

1. 收集：globalScope内的funcdef，classdef

2. 每个classDefNode中的funcdef，vardef

3. 注意内建函数和内置类型，在初始化globalScope时需放入其中（e.g. 字符串的处理）

#### 内置类型

`bool`, `int`, `string`

不放入`globalScope`的`classMap`，在sementic check的上海特判

#### 内建函数

**size函数**：对于数组类型的`MemberExpr`特判`size`函数处理

- 函数：`void print(string str);`
  - 作用：向标准输出流中输出字符串 `str`
- 函数：`void println(string str);`
  - 作用：向标准输出流中输出字符串 `str`，并在字符串末输出一个换行符
- 函数：`void printInt(int n);`
  - 作用：向标准输出流中输出数字 `n`
- 函数：`void printlnInt(int n);`
  - 作用：向标准输出流中输出数字 `n`，并且在数字末输出一个换行符
- 函数：`string getString();`
  - 作用：从标准输入流中读取一行字符串并返回
- 函数：`int getInt();`
  - 作用：从标准输入流中读取一个整数，读到空格、回车符、制表符处停止，返回该整数
- 函数：`string toString(int i);`
  - 作用：把整数 `i` 转换为字符串并返回
- 函数：`<identifier>.size()`
  - 返回数组长度`int`，调用数组的值不为null
- 函数：`int length();`
  - 使用方式：`<StringIdentifier>.length();`
  - 作用：返回字符串的长度。
- 函数：`string substring(int left, int right);`
  - 使用方式：`<StringIdentifier>.substring(left, right);`
  - 作用：返回下标为`[left, right)`的子串。

- 函数：`int parseInt();`
  - 使用方式：`<StringIdentifier>.parseInt();`
  - 作用：返回一个整数，这个整数应该是该字符串的最长前缀。如果该字符串没有一个前缀是整数，结果未定义。如果该整数超界，结果也未定义。

- 函数：`int ord(int pos);`
  - 使用方式：`<StringIdentifier>.ord(pos);`
  - 作用：返回字符串中的第pos位上的字符的ASCII码。下标从0开始编号。

常量字符串不具有内建方法，调用常量字符串的内建方法为未定义行为。

### semanticChecker

#### 类型检查

##### 类型推断

在`exprNode`中存`type`并进行推断

##### 类型匹配

函数调用的参数、函数返回值的类型

操作符对应的类型

**右值问题**（右值不能被赋值）

#### 作用

声明冲突

#### 控制流语句

break continue等