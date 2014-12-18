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

# 6 Arrays

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

# 7 Slices

Slice是go语言的一个关键数据类型，功能比array丰富，创使用make创建。

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

## 7.4 copy

```
c := make([]string, len(s))
copy(c,s)
```

## 7.5 slice

- 包含s[2],不包含s[5]
 - `l :=s[2:5]`
- 不包含s[5]
 - `l = s[:5]`
- 包含s[2]
 - `l = s[2:]`

## 7.6 定义和初始化

`t := []string{"g", "h", "i"}`

## 7.7 多维slice

`twoD := make([][]int, 3)`

# 8 Maps

## 8.1 定义

`m := make(map[string]int)`

## 8.2 赋值

```
m["k1"] = 7
m["k2"] = 13
```

## 8.3 获取

`v1 :=m["k1"]`

## 8.4 删除

`delete(m,"k2")`

## 8.5 定义和初始化

`n := map[string]int{"foo": 1, "bar": 2}`

# 9 Range

```
  nums := []int{2, 3, 4}
    sum := 0
    for _, num := range nums {
        sum += num
    }
    fmt.Println("sum:", sum)
```
```
    for i, num := range nums {
        if num == 3 {
            fmt.Println("index:", i)
        }
    }
```
```
    kvs := map[string]string{"a": "apple", "b": "banana"}
    for k, v := range kvs {
        fmt.Printf("%s -> %s\n", k, v)
    }
```
```
    for i, c := range "go" {
        fmt.Println(i, c)
    }
```

# 10 函数

```
package main

import "fmt"

func plus(a int, b int) int {

    return a + b
}

func main() {
    res := plus(1, 2)
    fmt.Println("1+2 =", res)
}
```

# 11 多返回值

```
package main

import "fmt"

func vals() (int, int) {
    return 3, 7
}

func main() {

    a, b := vals()
    fmt.Println(a)
    fmt.Println(b)

    _, c := vals()
    fmt.Println(c)
}

```

# 12 可变参数函数

```
package main

import "fmt"

func sum(nums ...int) {
    fmt.Print(nums, " ")
    total := 0
    for _, num := range nums {
        total += num
    }
    fmt.Println(total)
}

func main() {

    sum(1, 2)
    sum(1, 2, 3)

    nums := []int{1, 2, 3, 4}
    sum(nums...)
}
```

