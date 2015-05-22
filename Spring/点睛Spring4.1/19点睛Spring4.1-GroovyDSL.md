## 19.1 Groovy DSL
- Spring 4.x的一个新特性是使用Groovy的语言来配置Spring的bean;
- 这意味着我们构造一个spring的bean又多了一种方式,包括如下:
 - xml配置
 - java config(@Bean)
 - @Component,@Service,@Repository,@Controller系列
 - Groovy DSL

## 19.2 示例

### 19.2.1 演示类

```java
package com.wisely.dsl;

public class DemoService {
	private String msg;

	public String sayHello(){
		return "hello "+msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}

```

### 19.2.2 使用groovy配置DemoService为bean

- DemoConfig.groovy


```java
import com.wisely.dsl.DemoService //import要注册为bean的类
//所有的bean的声明放在beans下
beans{
//demoService为bean name,DemoService为类本身,msg = "world"为注入的属性
	demoService(DemoService){
		msg = "world"
	}
}

```

### 19.2.3 测试

```java
package com.wisely.dsl;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:com/wisely/dsl/DemoConfig.groovy")
public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
               new AnnotationConfigApplicationContext("com.wisely.dsl");
		DemoService ds =context.getBean(DemoService.class);
		System.out.println(ds.sayHello());
		context.close();
	}

}

```

输出结果

```java
Hello World
```