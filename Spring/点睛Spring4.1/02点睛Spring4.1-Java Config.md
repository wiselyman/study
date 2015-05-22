## 2.1 java config

- spring的java config主要使用**@Configuration**和**@Bean**两个注解;
 - 使用**@Configuration**注解在类上声明是一个配置类(相当于一个spring的配置xml);
 - 使用**@Bean**注解在方法上,返回值是一个类的实例,并声明这个返回值是spring的一个bean,bean的name是方法名;

## 2.2 关于@Bean和@Component,@Service,@Repository,@Controller

- `@Component,@Service,@Repository,@Controller`注解在一个类上之后,这个类也成为spring容器中的bean,使用@Bean注解也是,感觉使用@Bean注解是不是更麻烦呢?

- 既然效果是等同的，那什么时候使用@Bean什么时候使用`@Component,@Service,@Repository,@Controller`系列呢？

- 这个原则就和我们当初混用xml配置和`@Component,@Service,@Repository,@Controller`时候一样:系统的全局配置(数据库配置,spring mvc配置,spring security配置等)使用java config(xml),业务相关的bean使用`@Component,@Service,@Repository,@Controller`系列。

- 在后面我们讲到一些全局配置的时候我们就会使用Spring的java config


## 2.3 演示

### 2.3.1 创建一个properties(test.properties)文件作为配置

```java
wisely.word = World
```

### 2.3.2 创建一个java class
```java
package com.wisely.javaconfig;

public class DemoService {
	private String word;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String sayHello(){
		return "Hello "+this.word;
	}

}


```

### 2.3.3 创建java config配置类
```java
package com.wisely.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration //声明是一个配置类
@PropertySource("com/wisely/javaconfig/test.properties")
public class DemoConfig {

	@Bean //声明是一个bean
	public DemoService demoBean(Environment environment){
		DemoService demoService = new DemoService();
		demoService.setWord(environment.getProperty("wisely.word"));
		return demoService;
	}
}


```
### 2.3.4 测试-初始化spring容器

```java
package com.wisely.javaconfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		//设定此包下的类被注册成spring的bean,
        //包含@Configuration,@Component,@Service,@Repository,@Controller
		AnnotationConfigApplicationContext context =
        					new AnnotationConfigApplicationContext("com.wisely.javaconfig");
		DemoService demoService = context.getBean(DemoService.class);
		System.out.println(demoService.sayHello());
		context.close();
	}

}

```

输出结果:`Hello World`



