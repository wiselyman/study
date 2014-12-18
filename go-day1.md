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

# 13 闭包

匿名函数

```
package main

import "fmt"

func intSeq() func() int {
    i := 0
    return func() int {
        i += 1
        return i
    }
}

func main() {
    nextInt := intSeq()

    fmt.Println(nextInt())
    fmt.Println(nextInt())
    fmt.Println(nextInt())

    newInts := intSeq()
    fmt.Println(newInts())
}
```

# 14 递归

```
package main

import "fmt"

func fact(n int) int {
    if n == 0 {
        return 1
    }
    return n * fact(n-1)
}

func main() {
    fmt.Println(fact(7))
}

```

# 15 指针

```

package main

import "fmt"

func zeroval(ival int) {
    ival = 0
}

func zeroptr(iptr *int) {
    *iptr = 0
}

func main() {
    i := 1
    fmt.Println("initial:", i)

    zeroval(i)
    fmt.Println("zeroval:", i)

    zeroptr(&i)
    fmt.Println("zeroptr:", i)

    fmt.Println("pointer:", &i)
}

```
输出结果
```
initial: 1
zeroval: 1
zeroptr: 0
pointer: 0x42131100
```

# 16 结构体

```
package main

import "fmt"

type person struct {
    name string
    age  int
}

func main() {

    fmt.Println(person{"Bob", 20})

    fmt.Println(person{name: "Alice", age: 30})

    fmt.Println(person{name: "Fred"})

    fmt.Println(&person{name: "Ann", age: 40})

    s := person{name: "Sean", age: 50}
    fmt.Println(s.name)

    sp := &s
    fmt.Println(sp.age)

    sp.age = 51
    fmt.Println(sp.age)
}

```

# 17 方法

```
package main

import "fmt"

type rect struct {
    width, height int
}

func (r *rect) area() int {
    return r.width * r.height
}

func (r rect) perim() int {
    return 2*r.width + 2*r.height
}

func main() {
    r := rect{width: 10, height: 5}

    fmt.Println("area: ", r.area())
    fmt.Println("perim:", r.perim())

    rp := &r
    fmt.Println("area: ", rp.area())
    fmt.Println("perim:", rp.perim())
}

```

# 18 接口

```
package main

import "fmt"
import "math"

type geometry interface {
    area() float64
    perim() float64
}

type square struct {
    width, height float64
}
type circle struct {
    radius float64
}

func (s square) area() float64 {
    return s.width * s.height
}
func (s square) perim() float64 {
    return 2*s.width + 2*s.height
}

func (c circle) area() float64 {
    return math.Pi * c.radius * c.radius
}
func (c circle) perim() float64 {
    return 2 * math.Pi * c.radius
}

func measure(g geometry) {
    fmt.Println(g)
    fmt.Println(g.area())
    fmt.Println(g.perim())
}

func main() {
    s := square{width: 3, height: 4}
    c := circle{radius: 5}

    measure(s)
    measure(c)
}

```

# 19 Errors
```
package main

import "errors"
import "fmt"

func f1(arg int) (int, error) {
    if arg == 42 {
        return -1, errors.New("can't work with 42")
    }

    return arg + 3, nil
}

type argError struct {
    arg  int
    prob string
}

func (e *argError) Error() string {
    return fmt.Sprintf("%d - %s", e.arg, e.prob)
}

func f2(arg int) (int, error) {
    if arg == 42 {
        return -1, &argError{arg, "can't work with it"}
    }
    return arg + 3, nil
}

func main() {

    for _, i := range []int{7, 42} {
        if r, e := f1(i); e != nil {
            fmt.Println("f1 failed:", e)
        } else {
            fmt.Println("f1 worked:", r)
        }
    }
    for _, i := range []int{7, 42} {
        if r, e := f2(i); e != nil {
            fmt.Println("f2 failed:", e)
        } else {
            fmt.Println("f2 worked:", r)
        }
    }

    _, e := f2(42)
    if ae, ok := e.(*argError); ok {
        fmt.Println(ae.arg)
        fmt.Println(ae.prob)
    }
}

```