## 18.1 Meta Annotation
- 元注解:顾名思义,就是注解的注解
- 当我们某几个注解要在多个地方重复使用的时候,写起来比较麻烦,定义一个元注解可以包含多个注解的含义,从而简化代码
- 下面我们用<<02点睛Spring4.1-Java Config>>里的源码进行元注解的改造

## 18.2 示例
### 18.2.1 spring注解分析
我们看看spring的@Service的源码:可看出@Service注解是由几个注解组合的包含**@Component**;

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {

	String value() default "";

}
```
### 18.2.2 自定义元注解

下面的例子不见得举得合适,但是可简单演示元注解的作用
#### 18.2.2.1 新建元注解
组合@Configuration,@PropertySource注解为一个注解@WiselyMetaAnnotation

```java
package com.wisely.meta;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("com/wisely/javaconfig/test.properties")
public @interface WiselyMetaAnnotation {

}

```
#### 18.2.2.2 去除已有配置
去除DemoConfig上的配置,使用新定义的组合元注解

```java
package com.wisely.meta;

import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

//@Configuration //声明是一个配置类
//@PropertySource("com/wisely/javaconfig/test.properties")
@WiselyMetaAnnotation
public class DemoConfig {

	@Bean //声明是一个bean
	public DemoService demoBean(Environment environment){
		DemoService demoService = new DemoService();
		demoService.setWord(environment.getProperty("wisely.word"));
		return demoService;
	}
}

```

#### 18.2.2.3 测试
```java
package com.wisely.meta;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext("com.wisely.meta");
		DemoService demoService = context.getBean(DemoService.class);
		System.out.println(demoService.sayHello());
		context.close();
	}

}

```

输出结果

```java
Hello World
```

与<<02点睛Spring4.1-Java Config>>结果保持一致

