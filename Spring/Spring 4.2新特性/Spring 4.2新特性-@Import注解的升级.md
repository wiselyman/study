## 3.1 @Import

- @Import注解在4.2之前只支持导入配置类
- 在4.2,@Import注解支持导入普通的java类,并将其声明成一个bean

## 3.2 示例

- 演示java类

```java
package com.wisely.spring4_2.imp;

public class DemoService {
	public void doSomething(){
		System.out.println("everything is all fine");
	}

}

```

- 演示配置

```java
package com.wisely.spring4_2.imp;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
@Configuration
@Import(DemoService.class)//在spring 4.2之前是不不支持的
public class DemoConfig {

}

```

- 运行

```java
package com.wisely.spring4_2.imp;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("com.wisely.spring4_2.imp");
		DemoService ds = context.getBean(DemoService.class);
		ds.doSomething();
		
	}

}

```

输出结果

```java
everything is all fine
```