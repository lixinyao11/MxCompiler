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
   }```

   

   

visit第一个expr时：%0 = call malloc(size * ptr + 4bytes)

%1 = getelementptr i32 %0 i32 -1

   lastExpr.value = %1

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

# Codegen

## structure

* sections

  * data: globalVar

  * rodata: stringLIiteral

  * insts

    * inst

    * operand

      * reg

      * imm(number)

      * label(br, call, j)

      * memAddr

        (*xx(sp)* or *xx(rd)*(stack))

## Inst

> lui rd, imm(%hi(b))

##### li rd, imm

##### la rd, symbol

##### j label

>auipc rd, imm
>
>jal rd, offset
>
>jalr rd, offset(rs1)

##### call label

##### ret

##### branch rs1, (rs2, )label

branch if:

1. `rs1 ___ rs2`
   1. `==` beq
   2. `!=` bne
   3. `<` (signed) blt
   4. `>=` (signed) bge
   5. `>` (signed) bgt
   6. `<=` (signed) ble
2. `rs1`
   1. `==0` beqz
   2. `!=0` bnez
   3. `<=0` blez
   4. `>=0` bgez
   5. `<0` bltz
   6. `>0` bgtz

##### lw rd, offset(rs1)

```
e.g. lw a2, %lo(a)(a0)
	 lw a1, 8(sp)
```

> lb, lh, lw

>  lbu, lhu

##### sw rs2, offset(rs1)

> sb, sh, sw

##### ArithImm rd, rs1, imm

* addi
* (slti, sltiu)
* xori, ori, andi
* slli, (srli, )srai

##### Arith rd, rs1, rs2

* add, sub， mul, div, rem
* sll, (srl), sra
* (slt, sltu)
* xor, or, and

##### set rd, rs

* `== 0` seqz
* `!= 0 ` snez
* `< 0` sltz
* `> 0` sgtz



## visit IR

### structDef

记录每个类的大小、元素的位置（每个变量都占用4bytes）

### GlobalPtr, StringLiteral

对应label（注意转义字符）

### IRFunction

> LocalVar: 只在函数定义的参数列表里出现时不是以数字命名的，其他都是单调递增的数字
>
> alloca的ptr不一定是localPtr，也可能是LocalVar
>
> --> 合并了LocalVar和 LocalPtr

需要知道：函数过程中用到了几个临时寄存器，需要作为callee保存几个寄存器，如果有函数调用，需要作为caller保存几个寄存器。

以及ir中的虚拟寄存器(LocalVar)，全部需要分配空间，用一个map对每一个进行唯一标号(同时也表示其对应空间的偏移量)

函数的最开始：分配栈空间，保存作为callee需要保存的寄存器

返回前：恢复栈空间，恢复作为callee保存的寄存器

访问函数的过程中如果遇到需要临时寄存器的，也记录下来

用stackmanager类记录每个irfunction内的虚拟寄存器、用到的临时寄存器、需要保存的caller、callee寄存器对应的标号。每进入一个函数都新开一个stackmanager

#### 需要的栈空间

1. 局部变量：全部存入内存

2. alloca：分配内存
3. 16bytes对齐（16的倍数）
4. 需要用到的callee寄存器(谁要改保存谁)，需要保存到栈，也需要计算空间

**先扫一遍函数，计算整个函数需要的栈空间大小，并对其中需要分配的所有东西（保存的寄存器和虚拟寄存器(局部变量)）进行一个函数内唯一的编号(map)，方便之后寻址**

进入函数时sp减小，返回前加回去

`addi sp, sp, 64`

准备参数

入参1~入参7放在a0-a7寄存器，多余部分放入栈（sp指向第一个放不下的参数）

保存caller寄存器的值

调用函数

call foo

使用寄存器前：保存callee寄存器的值

返回：恢复callee寄存器，返回a0



block名字：entry：函数名

​					其他：函数名_block name

# Optimize

## BuildCFG

在每个block里保存前驱、后继(set, **不允许重复**)

* 每个block最多两个后继（brInst）

* 最后一条指令为
  1. `ret`: exit block, no succ
  2. `jump`: 1 succ
  3. `br`: 2 succ
* 没有前驱的block = function的第一个block = entry

## Mem2Reg

### reverse postorder(RPO)逆后序

visit as many of a node's preds as possible before visiting the node itself

**前序遍历**：dfs，先父后子，先根后子

**广度优先**：bfs

**后序遍历**：dfs，先子后父

**逆后序**：先得到后序遍历的顺序，再反过来遍历即可（顾名思义）

### Dominance

#### idom

利用迭代算法得出每个block的`idom`，idom只有一个，把该block的引用存放在block里

初始值：IDOM(n0) = {n0}, DOM(n) = null

```
for all nodes b: idom[b]=undefined
idon[entry]=entry
```

迭代：

参见[构造Dominator Tree以及Dominator Frontier_dominatortree_电影旅行敲代码的博客-CSDN博客](https://blog.csdn.net/dashuniuniu/article/details/52224882)

#### DomianceFrontier(DF)

每个block中一个**set**存它所有的df

参见[构造Dominator Tree以及Dominator Frontier_dominatortree_电影旅行敲代码的博客-CSDN博客](https://blog.csdn.net/dashuniuniu/article/details/52224882)

### place phi functions

对每一个function：

ArrayList<String> allocaNames

Hashmap<String, HashSet<IRBlock>> defBlocks

HashMap<String, Stack<int>> NumStack

HashMap<String, IREntity> valueStack

#### AllocaNames

所有alloca出来的变量名存在一个list中（整个function）

>  不管后续是否用到，都会插入phi，之后活跃分析后会消除不必要的def(也包括这里插入的phi)

对每一个alloca出来的name，存一个set保存所有包含了它的def(store语句)的block，作为phi-insertion的初始列表

#### insert phi functions

实现效果：alloca，store和load都不动，只添加phi语句

```
%x = alloca i32
%1 = load %x, i32
```

插入`%x = phi i32 [%x, %pred1.label], [%x, %pred2.then]` 

```
for each name x in GlobalName {
	WorkList = Blocks(x)
	for each block b in WorkList {
		for each block d in DF(b) {
			if d has no phi for x {
				insert a phi x = phi(x, x), blocks: two preds
				WorkList = workList and {d}
			}
		}
	}
}
```

### renaming variables

**短路求值也会产生phi，在这里不需要处理！已经是ssa**

对所有的AllocaNames，存一个编号栈和值栈

前序遍历支配树

对每个block，先把**除了短路求值生成的以外**每个phi指令的result改为%_xxx.n（n为这个allocaName的最新编号+1），并将这个localVar存入值栈。再遍历每条指令：遇到alloca指令，直接删去；遇到store指令(且store对象在allocaNames中)，在该name对应的值栈中加入这一entity，并删去指令；遇到load指令(...)，用值栈栈顶的值替换load指令的结果变量，如果值栈为空，throw exception。最后，遍历这个block**在CFG中的**所有后继block，把后继block中**除了短路求值生成的以外**每条phi指令的参数中，对应本block的那个改为值栈栈顶。

接着递归访问支配树上的所有孩子(需要在IRBlock里存一下支配树上的所有儿子)，访问结束后，把**值栈**（编号栈不改）都恢复到访问本block之前的状态。

### 消除phi （IR->ASM)

##### 大部分phi

在每个对应的前驱中插入mv指令即可（在ASMBuilder visit phiInst时处理，向每个前驱block的最后加一句mv）

#### critical edge

在所有critical edge中间插入一个新的基本块，修改父块的br指令和子块的phi指令中对应的block

之后即可正常消除phi，不会引起数据冲突

#### 在middleend消除critical edge(in Mem2Reg)

dfs CFG，找到所有critical edge并添加blank block，其中添加jump语句，修改前驱的exitInst和后继的phiInst

## 指令选择

先遍历完所有block，最后统一visit phi（在pred block末尾加mv）

所有局部变量、常量、全局变量都要新建一个virtual reg存入指令（reg类派生出virtual和physical）

需要维护一下sp，目前只放溢出的函数传参

### 创建虚拟寄存器

遇到literal或global：当场创建新的virtualReg（一次性使用）

在一个function内对所有的localVar：创建一个map<String, VirtualReg>

函数传参：超过8个后存在栈里

### 物理寄存器

函数返回：返回地址ra，返回值a0，函数参数a0-a7直接放入物理寄存器

### 函数调用

在call时，如果有溢出参数，将其推入sp的下方，第8个参数在最下面，最后一个参数在最上面，并在call之前把sp移到最下方（参数在sp上方），call完后恢复sp

在进入函数时，先用新的virtualReg mv出a0-a7里的参数，或者load出溢出的参数，再移动sp（本函数所需的栈空间，不包括溢出的参数），再保存本函数用到的tmp regs

在退出函数时（每一句ret前）,恢复sp，恢复用到的regs

### mark

有关sp的移动和regs的保存等需要注意之后再修改

某些操作如icmp需要一个中间量，则开了一个没有对应localvar的虚拟寄存器



## 活跃分析

得到每个基本块的livein，liveout，use，def
$$
in[n] = use[n] \cup (out[n] - def[n]) \\
out[n] = \cup _{s \in suc[n]} in[s] \\
out[exit] = \empty
$$

### CFG

继承ir的cfg即可

在ASMBlock里存对应的IRBlock，null表示startBlock

遍历所有block，

如果为startBlock，保存index，设置于下一个block（entry）的关系

否则，copy对应irblock的所有前驱后继

### use, def

在ASMBlock中保存两个set<VirtualRegister>作为use和def

对每个block：

```
for (each inst: x <- y op z)
	if y not in def: add y to use
	if z not in def: add z to use
	add x to def
```

### livein, liveout

先通过startBlock找到一个函数内的所有block的编号范围

对于每个函数内的所有block

```
changed = true;
while (changed) 
	changed = false;
	for (each block in function)
		recompute liveOut(block) and liveIn(block)
		if (liveOut(block) changed)
			changed = true
```


## 寄存器分配

尽可能地将临时变量分配到寄存器中

```
liveAnalysis

for eachFunction:
	build graph
	init worklists
	do {
		simplify
		coalesce
		freeze
		select spill
	} while()
	assign colour
	if (spilled) rewrite, continue
	else break
```

### 建冲突图(Build)

一个函数一张图

**无向**图，节点为变量，边表示冲突

遍历每条指令，所有def向此时存活的其他变量（这条指令的所有def和liveout）连边

```
for each BB
	liveNow = liveOut(BB)
	for each inst: C = a,b
		for each i in liveNow(postorder): 
			addedge(c,i) 
		liveNow.remove(c)
		liveNow.add(a,b)
```

### initAll

遍历每个block，每条指令，取出每个register

1. 把物理寄存器放入precolored，初始化color，度数为无限大
2. 虚拟寄存器放入initial，每个开一个adjList，度数为0
3. 每个寄存器的degree为0，开一个moveList

### 简化(Simplify)

1. 选择一个度数<k且传送无关的节点，把它和它的边移除，节点入栈**selectStack**
2. 迭代直到所有低度数且传送无关的节点都入栈（清空simplifyWorkList）

### 合并 Coalesce

对worklist中所有指令：mv, u->v



如果指令的rd和rs间无边，且a的每个邻居t的度数小于k或t与b冲突，将两点合并

合并u，v：把v并到u上，**相当于删除了v这个虚拟寄存器**

​		记录：son(u).add(v)，fa(v)=u

​		把v的所有连边连到u上（不重复）

​		如果u仍为低度数，simplufyList.add(u)

​		把这条mv指令删去

​		如果u不再传送相关，从freezeList里删去

​		把v从freezeList和spillList里删去

else，若无法合并这条传送，

### rewrite

给每个spilledNodes分配一块栈空间

在每个use、def前加上相应的lw，sw，并添加新的虚拟寄存器

保存这些虚拟寄存器

修改移动sp的语句



在ASMFunction中：存虚拟寄存器的cnt，存最开始的移动sp语句和所有ret前的移动sp语句，存指令选择完成后的stackSize

## inline

在 IR 上做

- count calling_time：在每个 FuncDef 中保存 calling_time 和 called_time
- 再遍历一遍所有callInst，对需要inline的callee inline

### 判断是否inline

只要满足任意一条就 inline：
1. callee.calling_times == 0
2. callee.called_times == 1

### 实现inline

需要修改：
1. callee 的 entry block 直接接在 caller 的 callInst 后面
2. callee 的 exit block 的 retInst 替换为 jump 到 caller 的 callInst 后面的指令
3. 指令的返回值用 phiInst 解决
3. 修改 callee 的 block 的前驱和后继
4. 修改 callee 的 block_name 和 var_name，避免重名
5. 修改 calling_times 和 called_times

为了避免多次inline导致的block/var重名，维护一个全局的inline_cnt，每次inline后加一

- block 改名为：block_name.callee_name.inline_cnt
- var（除函数形参外）改名为：var_name.callee_name.inline_cnt
- 新建的 caller 的返回过去的 block 命名为：callee_name.ret.inline_cnt

参数和返回值的处理：

- 参数：一开始就建立一个 callee.arg->caller.arg 的 map，在后续复制inst的过程中检查并替换
- 返回值：复制完 callee 以后，再新开一个block，第一句语句是 phi，接受返回值（如果有）接下来是原 callinst 后的所有 inst

block 之间的链接

- 建立一个 callee.block->caller.newblock 的 map
- 每复制一个block，就找到它的pred/succ中所有已经复制了的block并建立链接
- entry block和caller block连接
- 所有 exitblock 和 后面的那个 caller block（以phi开头的）连接

实现：

1. 建立好两个 map
2. 建立好 retblock，并链接好 succs，删去 caller_block 的 succs
3. 先遍历一遍所有block，建立好blockMap
1. 复制 callee 的每一个 block，并且 connect blocks：
   3. 复制 inst，替换 var（改名/换成形参
   4. 如果 exitInst 是 ret 还要进一步处理
      1. 替换为 jump 到 retBlock
      2. 保存返回值和当前block（为phi做准备）
1. 加入phiInst
1. 把call以后的指令放到retblock里
2. 在 原本的 block 中删去 call 及以后的inst
2. 加入 retblock
2. inline_cnt++
