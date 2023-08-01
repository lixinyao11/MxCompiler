### instanceof

二元运算符

`a(object) instanceof B(class)`

a的类型是B或B的子类：true，否则false

### super

类似`this`

1. `super.`代表子类中覆盖了父类型的部分，如子类覆盖了父类函数但依然想使用父类对应函数时，可用`super.func`显式调用

2. `super()`只能出现在构造函数的第一行

   类似于`this()`在构造函数的第一行调用**本类**对应的构造函数

   `super()`调用父类对应的构造函数，初始化父类型特征，实现代码复用

   当子类构造函数没有`super()`语句时，会默认加上无参数的`super()`语句，先构造父类部分

   当调用父类构造方法需要参数时，可以显式调用`super(a)`

### ArrayList

可以动态修改的数组

```java
import java.util.ArrayList; // 引入 ArrayList 类

ArrayList<E> objectName =new ArrayList<>();　 // 初始化
```

```java
array.add(element);
array.get(1); // 访问第2个元素(0-based)
array.set(2, element); // 修改第3个元素为element
array.remove(3);
array.size();
array.contains(element); // 相当于find
array.indexOf(element);
for (String i : array) { // 遍历
	System.out.println(i); // println自动输出
}
```

#### sort()

```java
import java.util.ArrayList;
import java.util.Collections;  // 引入 Collections 类

public class RunoobTest {
    public static void main(String[] args) {
        // 初始化ArrayList
        ArrayList<String> sites = new ArrayList<String>();
        sites.add("Taobao");
        sites.add("Wiki");
        sites.add("Runoob");
        sites.add("Weibo");
        sites.add("Google");
        Collections.sort(sites);  // sort()字母排序
        for (String i : sites) {
            System.out.println(i);
        }
    }
}
```

### HashMap

无序的，不会记录插入的顺序

```java
import java.util.HashMap; // 引入 HashMap 类
HashMap<Integer, String> Sites = new HashMap<Integer, String>();
```

```java
map.put("key", "element");
map.get("keyy");
map.remove("KEY");
for (String i : map.keySet()) {}
for (String i : map.values()) {}
```

