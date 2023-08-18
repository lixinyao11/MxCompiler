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
7. 同一作用域，函数名和变量名不允许重复，但是变量名和类名可以重复

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

#### FuncScope extends Scope

保存returnType, isReturned，处理函数的return问题

#### LoopScope extends Scope

处理continue，break问题

### symbolCollector

先visit一遍AST，解决前向引用的问题

1. 收集：globalScope内的funcdef，classdef

2. 每个classDefNode中的funcdef，vardef

3. 注意内建函数和内置类型，在初始化globalScope时需放入其中（e.g. 字符串的处理）

#### 重名问题

1. globalScope中：

   1. 函数名不与类名重复

      SemanticChecker: visit FuncDefNode时先检查

   2. 函数名不能与变量名重复

      

   3. *类、函数名分别各自不重复*

      *SymbolCollector*

   4. 变量名分别各自不重复

      SemanticChecker

2. classScope中：

   1. 变量名可以覆盖全局变量名、类名、函数名

   2. 函数名可以覆盖全局的所有名字

   3. 变量名与函数名不重复 

      SemanticChecker：visit FuncDefNode时先检查

   4. *变量名、函数名分别各自不重复* 

      *在SymbolCollector中处理*

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

#### new Scope

1. BlockStmt
2. FuncDef(visit paraList之前)
3. ClassDef (classScope)
4. thenStmt, elseStmt
5. statement in WhileStmt
6. ForStmt

#### visit VarDef: 

1. in globalScope: 
   1. 检查type是否是内建类型或定义过的class
   2. 依次遍历每个变量
      1. 检查是否与全局函数重名
      2. 检查是否与已有全局变量重名
      3. visitExpr，判断type是否一致
      4. 加入vars
2. in classScope: 
   1. 检查type是否是内建类型或定义过的class
   2. 依次遍历每个变量
      1. visitExpr，判断type是否一致
3. in Scope:
   1. typeCheck
   2. 依次遍历每个变量
      1. 检查是否与currentScope的vars重名
      2. 判断Expr的type
      3. 加入vars

#### Expr:

visit过程中进行类型检查和类型推断，保存每个`ExprNode`的`type`和`isLeftValue`

遇到变量时，递归向上查找变量是否被定义过，找不到变量为error

遇到自定义类型，直接查找`globalScope`

遇到函数，递归向上查找，遇到`classScope`和`globalScope`都尝试查找，如果`globalScope`也没有说明找不到定义

##### callExpr

检查func是function，用type中保存的FuncDecl检查paras

##### AtomExpr

对`identifier`

变量名或函数名

向上递归查找，若为variable，保存类型。

若为function，保存FuncDef(可能在globalScope或classScope中找到)

##### BinaryExpr

1. `+, -, *, /,%`
2. `> < >= <=`
3. `== !=`
4. `&& || !`
5. `>> << & | ^ ~`

1. 表达式两边的对象类型必须一致。而表达式两边的值可以是常量或变量
2. `bool` 类型仅可做 `==`、`!=`、`&&` 以及 `||` 运算
3. 数组对象仅可以和数组以及常量 `null` 进行 `==` 和 `!=` 运算
4. 类对象的 `==` 和 `!=` 运算为比较内存地址，其它运算符重载是未定义的
5. 字符串
   1. `+` 表示字符串拼接
   2. `==`，`!=` 比较两个字符串内容是否完全一致（不是内存地址）
   3. `<`，`>`，`<=`，`>=` 用于比较字典序大小
   4. 字符串参与其他双目运算为语法错误，且字符串仅可与相同类型对象进行运算（和`null`也不可以）

##### AssignExpr

1. 将字符串对象赋值为 `null` 是语法错误

2. 左值
   1. 函数的形参变量
   2. 全局变量和局部变量
   3. 类的一个成员
   4. 数组对象的一个元素
3. 非法左值：
   1. this
   2. 常量

string == null

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

# IR

## IR design

### IRProgram

1. 全局变量 `@<variablename> = global <type> value  ` 指针
2. 自定义struct`%<classname> = type { <type>+ }`po0[--0-]
3. 函数`define <returnType> @<funcname>(<type> [name], ...) {basic blocks}`

#### GlobalVarDef

#### StructDef

#### Function

retType, paras, name, body

存map<String, int>记录每个名字的出现次数，只增不减

存一个cnt作为函数内临时变量%0, %1, %2 ...的计数

#### block

`parent(Fucntion), label(string), instructions(list<IRInst>)`

### Type

`int`, `ptr`, `void`

bool -> i1

### IREntity

#### IRVariable

1. globalPtr
2. localPtr
3. localVar

#### IRLiteral

1. null
2. int (including true, false)
3. **string**

### IRScope

`arraylist<string>`只存原变量名，不带`".1"`

`className`若在class内，存类名

`loopType`：不在循环内：0，while：1，for：2

`loopNum`：循环的label编号

`map<String, int>`存该scope内定义过的所有var和对应的编号，在遇到AtomExpr事访问此map查找var的编号

### Inst

#### Alloca

```java
LocalPtr result;
IRType type;
```

#### Store

```
IREntity src = null;
IRVariable pos = null; // must be ptr type
```

#### Binary

必然仅在BinaryExpr中调用-->result必为LocalVar，type即result.type

两个运算数，可能是变量或字面量



## IRBuilder

保存：`globalScope`, `currentBlock`，`currentScope`

### visit Program

1. VarDefNode
2. FuncDefNode
3. ClassDefNode

### visit VarDefNode

#### in global scope

对每一个var：检查是否有初始值/初始值是否为常量，构建`globalVarDef`放入`IRProgram`

如果需要初始化，检查`IRProgram`里是否有`_init`函数，没有则创建

`void _init() {...}`

将`current_block`改为`_init: entry block`，visit初始化的`ExprNode（结果存在targetVar中，再store回globalPtr

#### in local scope(must be in function, can't be in class)

先处理重名问题(scope中记录新名字，在function中找到序号，更名)

再alloca创建localPtr

​	如有初始值，继续访问expr，最后store回去

### visit FuncDef

构造函数自动创建entry block

修改currentBlock，新建currentScope

**如果在class内：需要改名，增加一个this参数**

visit ParaList处理参数（值传递vs引用传递）

visit stmts处理语句

离开func后：离开scope，currentBlock = null（不用调用exit）

（之后每次需要使用变量时就新开一个局部变量`%1，%2...`并load

### visit classDef

对内部的VarDef: 直接从ClassScope中获取所有memType，创建structDef并放入irProgram

先开一个scope，保存className

依次visit构造函数和其他函数

退出时，scope = null，回到global（不需要调用exit，scope中没有定义变量）

### visit ParaList

**if in class**: add a **this** parameter

将剩余parameter加入对应IRFunction

先对每一个参数进行重名操作，在scope中记录新名字，在function中找到序号（必为0），并序号更新为1（`this`参数也需要同样操作）

添加若干**alloca**和**store**语句完成替换，此后入参与函数体内的局部变量无异，都需要通过被覆盖的新名字对应的**localPtr**来访存读写。

### visit ClassBuild

类似class内的FuncDef

需要改名，增加一个this参数，并对this参数进行%this.1的alloca和store

也需要将函数加入`IRProgram`中，新开一个scope，并更新currentBlock为函数的entryBlock

然后依次遍历stmt即可

退出时current_scope = parent(因为是退出函数，不需要调用exit)，currentBlock = null

### visit StmtNode

#### BlockStmt

开一个新的scope（继承)，原currentBlock不变

遍历visit所有stmt

退出后`scope.exit(); scope = parent;`

#### IfStmt

先再本scope，本currentblock计算condExpr，结果存储到一个localVariable(i1)中，之后（如果有then）

```
br i1 %4, label %if.then.0, label %if.else.0
```

再新开一个`if.then`block，同时新建scope，访问thenStmt，最后无条件的跳转到if.end。退出后scope.exit

再开一个`if.else`block，再新建scope，访问elseStmt，最后无条件跳转到if.end，退出后scope.exit(若有elseStmt)

最后开一个`if.end`block，scope依然是if外的scope，结束对ifStmt的访问

#### WhileStmt

在进入whileStmt之前就无条件跳转到%while.cond

开一个while.cond基本块，scope不变，访问condExpr，用一个i1的临死变量存储结果，再

```
br i1 %3, label %while.body.0, label %while.end.0
```

开一个while.body基本块，开一个新scope（带loop），访问body，最后无条件跳转到while.cond，退出后scope.exit

新开一个while.end基本块，scope依然是whileStmt外的scope，结束对WhileStmt的访问

#### forStmt

先新开一个scope（带loop），currentblock不变，完成init（alloca，store）

之后无条件跳转到for.cond.0基本块

新开一个for.cond基本块，访问condExpr，用一个i1临时变量存储结果

```
br i1 %3, label %for.body.0, label %for.end.0
```

再新开一个for.body基本块，访问循环体，最后

```
br label %for.step.0
```

开一个for.step基本块，访问stepExpr（完成计算后store）最后

```
br label %for.cond.0
```

最后scope.exit，开一个for.end基本块，处在forStmt外的scope内，结束访问。

#### ContinueStmt

需要知道当前在什么loop里，以及该loop的序号是多少

* for循环：`br label %for.step.0`
* while: `br label %while.cond.0`

#### BreakStmt

* 在for循环中：`br label %for.end.0`
* while: `br label %while.end.0`

#### ReturnStmt

先访问return后的expr，生成存储返回值的临时变量

```
ret i32 %2
```

#### ExprStmt

currentBlock和currentScope都不变

expr的返回值丢弃，targetVar = null

直接访问expr即可

#### VardefStmt

直接访问其中的VarDefNode即可

### ExprNode

均不需要更改currentScope，currentBlock

把最后的返回值存在lastExpr里（有可能用不到，有可能用得到）

若为右值，只存一个返回值%1（里面是int/ptr等），也可能是literal

若为左值，存一个返回值和一个ptr（变量本身的ptr，可能是local/globalPtr，也可能是localVar（A.b, x[1]）

若为函数，构建一个funcInfo（包含函数名(带类名`A::`)，retType，this(ptr if needed))

#### BinaryExpr

依次访问左右两个expr，得到lhsVar和rhsVar（存的是被操作数里的值，即int）

+-*/% << >> & | ^：左右type与最终type均相同，直接使用对应binaryInst

== != >= > <= <：左右type相同，直接使用icmpInst，返回类型为i1

&& ||：**需要短路求值** 左右都是i1，直接& |，得到i1

&&：先visit lhs，

br lhs.value, if.then.no, if.else.no

if.then.no: visit rhs, 返回得到的lastExpr.value

br if.end.no

if.else.no：存一个常量false，返回该var

br if.end.no



||：先visit lhs

br lhs.value,  if.then.no, if.else.no

if.then.no：存一个常量true，返回该var

if.else.no：visit rhs，返回得到的value

不是左值，返回一个值

#### UnaryExpr

均非左值，只返回一个value

先访问expr，获得lastExpr

比如对局部/全局变量操作，exprVal就是Local/GlobalPtr

`! ~` : xor with true / -1(1...11)

`+` : add with 0

`-` : sub with 0

`++ --` (`a++`) : lastExpr.value里存了a的值，因此lastExpr.value不用变，继续作为返回值。再用这个值加一/ -1，存到一个新的localVar(retVar)（add语句）最后把它store回lastExpr.dstptr

#### PreSelfExpr

（是左值，需要返回ptr）`++a, --a`

先访问其中的expr，得到a对应的value和ptr

用value +/- 1，得到一个临时变量tmp

将tmp store回ptr中

返回值为tmp，返回的ptr不变

#### AssignExpr

lhs为左值，左右类型相同

先后访问lhs和rhs，把rhs得到的value store进lhs的destptr里

返回的是右值，只需返回rhs的value，相当于不变

#### ConditionalExpr

先访问condExpr

返回值应当是一个i1

```
br i1 %4, label %cond.then.0, label %cond.else.0
```

再新开一个`cond.then`block，访问thenExpr(返回值丢弃)	，最后无条件的跳转到if.end

再开一个`if.else`block，访问elseExpr（返回值丢弃），最后无条件跳转到cond.end

最后开一个`if.end`block，结束访问

#### CallExpr

```
%result = call i32 @foo(i32 %arg1)
call void @foo2(i8 97)
```

若为void函数，则visit后没有返回值

否则返回一个临时localVar，存储call的result(是右值)



先visit funcExpr，返回一个string为函数名（若为成员函数，包括"A::"）还有一个`IRType retType`以及`this ptr`

再依次visit args，每一个expr都会返回一个value（临时/ptr/literal），依次存入Call中

最后生成CallInst，如果不是void，返回value = result，否则value = null;

#### MemberExpr

1. ```
   node.obj.type.dim > 0
   ```

   对于数组.size()，当场解决，

   先visit expr

   %0 = getelementptr i32 ptr, %lastExpr.value i32 -1

   load i32 %0

   非左值，把load出的值放入value即可

2. 对于string.func()，放在class里一起 **"string::func"**

3. 对于class.mem：先去globalScope拿到classDecl

   1. 若mem为function：`A.f()`

      返回值仅有一个string funcName(加上`A::f`)和一个`retType`还有一个`ptr this`（funcInfo）

   2. 若mem为var：`A.b`返回值为左值，先visit obj，拿到的value为该对象指针。getelementptr获取成员指针，存入destptr，并load出value存入lastExpr.value

#### AtomExpr

直接检查ExprType

1. isFunc:(**只能是全局函数**) 返回string（全局函数->原名），retType, this == null
2. isLeftValue: this/variable 
   1. 去globalScope检查，若为全局变量，返回原名对应的GlobalPtr
   2. 去currentblock.parent.idCnt找到其对应的新名字，返回新名字对应的LocalPtr
      1. 对ptr进行load（根据exprType里存的类型），返回得到的value，以及ptr本身

1. notleftValue: Null/intConst/StringConst/TrueFalse
   1. null/intConst/trueFalse: 生成对应的IRLiteral存入lastExpr.value
   2. StringConst: 在全局生成一个存储了该变量的GlobalPtr(i8 数组)，名字为string.1，string.2，之后返回这一GlobalPtr即可（作为value，因为string本身就是ptr类的）

#### NewExpr

class单个对象或数组，返回均非左值

* 调用c函数

  int[20], bool[20]：直接存数组

  A[20]：存20个ptr，每个指向一个A，并调用构造函数

  string[20]：存20个ptr，值为null，不用进一步开循环

  高维数组：开到最后一层位置，留null

1. 单个class：%2 = alloca %class.A

   再call A::A(ptr %2)，返回%2作为value

2. 数组：`int a[x][]; A a[1]; string a[2][4][5];`

   依次visit每一个expr时：设置一个函数

   ```
   saveretValue
   if (Expr != null) {
   	visit expr
   	size = lastExpr.value
   	%0 = call malloc(size * 4 + 4bytes) 
   	%1 = getelementptr i32 %0, i32 1
   	
   	int no = forCnt++;
   	%2 = alloca i32
   	store i32 0, ptr %2 //i = 0
   	br label for.cond + no
   	
   	currentBlock = addBlock: for.cond.no
   	// i < size
   	%0 = load i32, ptr %i
     %cmp = icmp slt i32 %0, size
     br i1 %cmp, label %for.body, label %for.end
   	
   	addBlock: for.body.no
   	%3 = getelementptr ptr %1 i32 %0
   	var(%1) = foo(nextExpr)
   	store var(%1) -> %3
   	br label %for.step.no
   	
   	Block: for.step.no
   	// i++
   	%3 = load i32, ptr %i, align 4
     %inc = add nsw i32 %3, 1
     store i32 %inc, ptr %i
   	br label %for.cond
   	
   	Block: for.end.no
   	return %1( = lastExpr.value)
   } else { // after the last expr
   // 被上一重循环调用，如int[4]循环内调用
   %1 = alloc (type in array)
   (if class type) call A::A(ptr %1)
   return %1
   }
   ```

   

   

   visit第一个expr时：%0 = call malloc(size * ptr + 4bytes)

   ​                                  %1 = getelementptr i32 %0 i32 -1

   ​                                  lastExpr.value = %1

   如果后面还有expr：visit, get size2

   ```
   for (int i = 0; i < x; ++i) {
   	%2 = call malloc(size2 + 4bytes)
   	%3 = getelementptr i32 %2 i32 -1
   	%4 = getelementptr ptr %1 i32 i
   	store %3 -> %4
   }
   ```

   如果是最后一个expr了：

   ```
   for (int i = 0; i < x; ++i) {
   	%2 = alloc i32(type in array)
   	if (type is class) call A::A(ptr %2)
   	%3 = getelementptr ptr %1 i32 i
   	store %2 -> %3
   }
   ```

#### ArrayExpr

```
A.b.c[1]
```

先visit expr，expr可能是右值

利用得到的value（ptr类型）

getelementptr type destptr i32 index;

返回值是左值

# todo-list 

1. refractor：ASTNode中尽量不要public
2. refractor：util/里的一些东西移到AST/
3. refractor：IRNode和Util不要public
4. 是否需要private？
5. 合并AST中的type？

# Mark Everyday

getelementptr ptr %0, i32 0不会解引用，会返回原ptr%0

转义字符

new 不能alloca，要malloc开一个全局map记录类型对应的size

只有malloc的时候会涉及到类型对应的字节数

ptr需要改为8个字节（64位机器）

短路求值：必须alloca一个i1来存最终的返回值
