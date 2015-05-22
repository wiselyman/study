## 9.1 AOP
- AOP可以了让一组类共享相同的行为.在OOP中只能通过继承类和实现接口,这样使代码的耦合度增强,且类继承只能为单继承,阻碍更多行为添加到一组类上;
- 下面演示一个日志系统的实现,简单但不失表达AOP的核心内容
 - 演示通过注解拦截和通过方法规则拦截;
- 一些小术语
  - JoinPoint:你需要拦截的代码位置(代码里已标识)
  - Pointcut:符合某个条件后需要执行的代码位置(代码里已标识)

## 9.2 示例

采取2种截获方式:拦截注解和拦截方法

### 9.2.1 增加aspectj依赖到maven

```xml
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.8.5</version>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.8.5</version>
</dependency>

```

### 9.2.2 编写拦截规则的注解

```java
package com.wisely.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {
	String name();

}

```
### 9.2.3 编写需要拦截的2个测试bean

- 使用注解

```java
package com.wisely.aop;

import org.springframework.stereotype.Service;

@Service
public class Demo1Service {
	@Action(name="demo1,add操作")
	public void add(){} //JoinPoint
	@Action(name="demo1,remove操作")
	public void remove(){}//JoinPoint
	@Action(name="demo1,update操作")
	public void update(){}//JoinPoint
	@Action(name="demo1,query操作")
	public void query(){}//JoinPoint

}

```

- 使用方法规则

```java
package com.wisely.aop;

import org.springframework.stereotype.Service;

@Service
public class Demo2Service {
	public void add(){}//JoinPoint
	public void remove(){}//JoinPoint
	public void update(){}//JoinPoint
	public void query(){}//JoinPoint
}

```

### 9.2.4 编写切面

```java
package com.wisely.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
	@After("@annotation(com.wisely.aop.Action)") //此处为pointcut
	public void after(JoinPoint joinPoint) {
		//每一个符合表达式条件的位置为joinPoint
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Action action = method.getAnnotation(Action.class);
		System.out.println(action.name());
		//获得操作内容后可插入数据库中
	}

	@Before("execution(* com.wisely.aop.Demo2Service.*(..))") //此处为pointcut
	public void before(JoinPoint joinPoint){
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		System.out.println("demo2,"+method.getName());

	}
}

```

### 9.2.5 测试

```java
package com.wisely.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy //开启对AspectJ的@Aspect注解的支持,别忘了
public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
        		new AnnotationConfigApplicationContext("com.wisely.aop");
		Demo1Service d1s = context.getBean(Demo1Service.class);
		d1s.add();
		d1s.remove();
		d1s.update();
		d1s.query();

		Demo2Service d2s = context.getBean(Demo2Service.class);
		d2s.add();
		d2s.remove();
		d2s.update();
		d2s.query();
		context.close();
	}

}

```

输出结果

```java
demo1,add操作
demo1,remove操作
demo1,update操作
demo1,query操作
demo2,add
demo2,remove
demo2,update
demo2,query
```

