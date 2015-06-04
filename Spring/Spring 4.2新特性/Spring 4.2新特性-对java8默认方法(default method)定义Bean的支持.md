## 2.1 默认方法(default method)
- java8引入了一个`default medthod`;
- 用来扩展已有的接口,在对已有接口的使用不产生任何影响的情况下,添加扩展
- 使用`default`关键字
- Spring 4.2支持加载在默认方法里声明的bean

## 2.2
- 将要被声明成bean的类

```java
public class DemoService {
	public void doSomething(){
		System.out.println("find bean in default method");
	}
}

```

- 在接口的默认方法里定义bean

```java
package com.wisely.spring4_2.defaultMethod;

import org.springframework.context.annotation.Bean;
public interface DemoServiceConfig {
	
	@Bean 
	default DemoService DemoService(){
		return new DemoService();
	}

}

```

- 配置类

```java
package com.wisely.spring4_2.defaultMethod;

import org.springframework.context.annotation.Bean;
public interface DemoServiceConfig {

	@Bean
	default DemoService DemoService(){
		return new DemoService();
	}

}

```

- 运行

```java
package com.wisely.spring4_2.defaultMethod;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	public static void main(String[] args) {
		 AnnotationConfigApplicationContext context =
	                new AnnotationConfigApplicationContext("com.wisely.spring4_2.defaultMethod");
		 DemoService ds = context.getBean(DemoService.class);
		 ds.doSomething();
	}
}


```

- 输出结果

```java
find bean in default method
```