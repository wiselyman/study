## 1.1 声明bean
- 使用上例建立的testMavenSpring项目,将pom.xml文件中的
`<spring-framework.version>3.2.3.RELEASE</spring-framework.version>`修改为`4.1.5.RELEASE`,
然后项目->右键->maven->update project;
- spring利用**@Configuration,@Component,@Service,@Repository,@Controller**注解在一个java类上声明是spring容器的bean;
- 使用**@Configuration,@Component,@Service,@Repository,@Controller**任意一个在类上效果是等同的,不同的名称是为了更好的标志类的角色和功能,避免代码维护者混淆代码作用;
- 那我们使用这四个注解的准则是什么呢?
 - @Configuration 声明是一个配置bean
 - @Component 系统组件,没有明确的角色;
 - @Service 在业务逻辑层(service层)使用;
 - @Repository 在数据访问层(dao层)使用;
 - @Controller 在展现层(MVC->Spring MVC)使用;

## 1.2 注入bean
- 可以使用**@Autowired,@Inject(JSR-330),@Resource(JSR-250)**注入用**@Component,@Service,@Repository,@Controller**(当然包括xml声明的或者用@Bean声明的bean)声明的bean;
- **@Autowired,@Inject,@Resource**在一般情况下是通用的;
- **@Autowired,@Inject,@Resource**可注解在set方法上或者属性上;我习惯注解在属性上,代码更少层次更清晰;

## 1.3 示例

### 1.3.1 新建待注入的java类
```java
package com.wisely.di;

import org.springframework.stereotype.Service;

@Service//写为@Component,@Controller,@Repository效果相同,视具体情况使用
public class Demo1Service {

	public String sayHello(String word ){
		return "Hello "+word;
	}
}


```

### 1.3.2 新建被注入的java类
```java
package com.wisely.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class Demo2Service {
	@Autowired //注入Demo1Service,还可使用JavaEE的@Inject(JSR-330),@Resource(JSR-250)效果相同
	Demo1Service demo1Service;
	public String callDemo1SayHello(String word){
		return demo1Service.sayHello(word);
	}

}

```

### 1.3.3 测试
```java
package com.wisely.di;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

	public static void main(String[] args) {
		//设定此包下的类被注册成spring的bean,包含@Configuration,@Component,@Service,@Repository,@Controller
		AnnotationConfigApplicationContext context =
        		new AnnotationConfigApplicationContext("com.wisely.di");
		Demo2Service demo2Service = context.getBean(Demo2Service.class);
		System.out.println(demo2Service.callDemo1SayHello("World"));
		context.close();
	}

}

```

输出结果`Hello World`