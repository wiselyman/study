## 4.1 @Order

- Spring 4.2 利用`@Order`控制配置类的加载顺序

## 4.2 演示

- 两个演示bean

```language
package com.wisely.spring4_2.order;

public class Demo1Service {

}

```

```language
package com.wisely.spring4_2.order;

public class Demo2Service {

}

```

- 两个配置类,注意@Order配置加载的顺序

```java
package com.wisely.spring4_2.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(2)
public class Demo1Config {
	@Bean
	public Demo1Service demo1Service(){
		System.out.println("demo1config 加载了");
		return new Demo1Service();
	}

}

```

```java
package com.wisely.spring4_2.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
public class Demo2Config {
	
	@Bean
	public Demo2Service demo2Service(){
		System.out.println("demo2config 加载了");
		return new Demo2Service();
	}

}

```

- 运行

```java
package com.wisely.spring4_2.order;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("com.wisely.spring4_2.order");
	}


}

```

输出结果

```java
demo2config 加载了
demo1config 加载了
```

读者可自己调整顺序在运行
