grammar Mx;

program: (funDef | classDef | varDef)* EOF;

classDef : Class Identifier '{' (varDef | classBuild | funDef)* '}' ';';

classBuild : Identifier '('')' suite;
funDef : returnType Identifier '(' paraList? ')' suite;
returnType : Void | type;
paraList : type Identifier (Comma type Identifier)*; // 默认值？

suite : '{' statement* '}';

statement
    : suite                                                                     #blockStmt
    | varDef                                                                    #vardefStmt
    | If expression trueStmt=statement
        (Else falseStmt=statement)?                                             #ifStmt
    | While '(' expression ')' statement                                        #whileStmt
    | For '(' expression? ';' expression? ';' expression? ')' statement #forStmt
    | Return expression? ';'                                                    #returnStmt
    | Break ';'                                                                 #breakStmt
    | Continue ';'                                                              #continueStmt
    | expression ';'                                                            #exprStmt
    | ';'                                                                       #emptyStmt
    ;

varDef : type Identifier (Assign expression)? (Comma Identifier (Assign expression)?)* Semi;
type : typeName ('['']')*;
typeName : baseType | Identifier;
baseType : Bool | Int | String;

expression
    : New typeName '[' expression ']' ('['']')*                                 #newArrayExpr
    | New typeName ('(' ')')?                                                   #newVarExpr
    | expression '(' (expression (Comma expression)*)? ')'                      #funcExpr
    | expression '[' expression ']'                                             #arrayExpr
    | expression op=Member Identifier                                           #memberExpr
    | expression op=(Increment | Decrement)                                     #unaryExpr
    | <assoc=right> op=(Increment | Decrement) expression                       #unaryExpr
    | <assoc=right> op=(Not | LogicNot | Minus | Plus) expression               #unaryExpr
    | expression op=(Mul | Div | Mod) expression                                #binaryExpr
    | expression op=(Plus | Minus) expression                                   #binaryExpr
    | expression op=(LeftShift | RightShift) expression                         #binaryExpr
    | expression op= (Greater | GreaterEqual | Less | LessEqual) expression     #binaryExpr
    | expression op=(Equal | UnEqual) expression                                #binaryExpr
    | expression op=And expression                                              #binaryExpr
    | expression op=Xor expression                                              #binaryExpr
    | expression op=Or expression                                               #binaryExpr
    | expression op=LogicAnd expression                                         #binaryExpr
    | expression op=LogicOr expression                                          #binaryExpr
    | expression '?' expression ':' expression                                  #conditionalExpr
    | <assoc=right> expression op=Assign expression                             #assignExpr
    | primary                                                                   #atomExpr
    ;

primary
    : '(' expression ')'
    | Identifier
    | literal
    | This
    ;

literal : True | False | IntegerConst | StringConst | Null;

Void : 'void';
Bool : 'bool';
Int : 'int';
String : 'string';
New : 'new';
Class : 'class';
Null : 'null';
True : 'true';
False : 'false';
This : 'this';
If : 'if';
Else : 'else';
For : 'for';
While : 'while';
Break : 'break';
Continue : 'continue';
Return : 'return';

Plus: '+';
Minus: '-';
Mul: '*';
Div: '/';
Mod: '%';

Greater: '>';
Less: '<';
GreaterEqual: '>=';
LessEqual: '<=';
UnEqual: '!=';
Equal: '==';

LogicAnd: '&&';
LogicOr: '||';
LogicNot: '!';

RightShift: '>>';
LeftShift: '<<';
And: '&';
Or: '|';
Xor: '^';
Not: '~';

Assign: '=';

Increment: '++';
Decrement: '--';

Member: '.';

Identifier: [a-zA-Z][a-zA-Z0-9_]*;

LParen: '(';
RParen: ')';
LBracket: '[';
RBracket: ']';
LBrace: '{';
RBrace: '}';

Question: '?';
Colon: ':';
Semi : ';';
Comma : ',';

Quote: '"';
Escape: '\\\\' | '\\n' | '\\"';
IntegerConst: '0' | [1-9][0-9]*;
StringConst: Quote (Escape | .)*? Quote;

WhiteSpace: [\t\r\n ]+ -> skip;

LineComment: '//' ~[\r\n]* -> skip;
ParaComment: '/*' .*? '*/' -> skip;

