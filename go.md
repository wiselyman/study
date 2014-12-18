# 1 变量

## 1.1 定义

`var f string="short"`

## 1.2 定义多个变量

`var b, c int = 1, 2`

## 1.3 缩写

`f :="short"`

# 2 常量

## 2.1 定义

`const s string = "constant"`

## 2.2数字型常量

数字型常量在没有明确赋值前没有类型。
`const n = 500000000`

# 3 for

```
 for j := 7; j <= 9; j++ {
        fmt.Println(j)
    }
```

# 4 if/else
```
package main

import "fmt"

func main() {

    if 7%2 == 0 {
        fmt.Println("7 is even")
    } else {
        fmt.Println("7 is odd")
    }

    if 8%4 == 0 {
        fmt.Println("8 is divisible by 4")
    }

    if num := 9; num < 0 {
        fmt.Println(num, "is negative")
    } else if num < 10 {
        fmt.Println(num, "has 1 digit")
    } else {
        fmt.Println(num, "has multiple digits")
    }
}
```

# 5 switch
```
 switch i {
    case 1:
        fmt.Println("one")
    case 2:
        fmt.Println("two")
    case 3:
        fmt.Println("three")
    }
```

# 6 arrays

## 6.1定义

`var a [5]int`

## 6.2赋值

`a[4]=100`

## 6.3数组长度

`len(a)`

## 6.4定义和初始化数组

`b := [5]int{1, 2, 3, 4, 5}`

## 6.5多维数组

`var twoD [2][3]int`

# 7 slices

slice是go语言的一个关键数据类型，功能比array丰富，创使用make创建。

## 7.1定义

`s := make([]string, 3)`

## 7.2 赋值

```
 s[0] = "a"
 s[1] = "b"
 s[2] = "c"
```

## 7.3 append

```
s = append(s, "d")
s = append(s, "e", "f")
```

# 7.4 copy

```
c := make([]string, len(s))
copy(c,s)
```