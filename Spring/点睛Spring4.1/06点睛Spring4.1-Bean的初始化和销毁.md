## 6.1 Initialization和Destruction
- spring对bean初始化的时候和销毁时候进行某些操作提供了支持
	- 利用@Bean的**initMethod**和**destroyMethod**(和xml配置的init-method和destory-method相同)
	- 利用JSR-250的**@PostConstruct**和**@PreDestroy**

## 6.2 示例
### 6.2.1 @Bean形式的Initialization和Destruction
#### 6.2.1.1 新建服务java类
```java
package com.wisely.prepost;

public class BeanWayService {
	public void init(){
		System.out.println("init-method-bean");
	}
	public BeanWayService() {
		super();
		System.out.println("初始化构造函数-bean");
	}
	public void destroy(){
		System.out.println("destory-method-bean");
	}

}

```
#### 6.2.1.2 新建配置java类
```java
package com.wisely.prepost;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class BeanWayConfig {
	@Bean(initMethod="init",destroyMethod="destroy")
	public BeanWayService beanWayService(){
		return new BeanWayService();
	}
}

```

#### 测试
```java
package com.wisely.prepost;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
        		new AnnotationConfigApplicationContext("com.wisely.prepost");
		context.close();

	}

}
```
输出结果
```java
初始化构造函数-bean
init-method-bean
destory-method-bean
```

### 6.2.2 JSR-250形式的Initialization和Destruction
#### 6.2.2.1 添加jsr250-api到maven依赖
添加如下到pom.xml
```xml
<dependency>
    <groupId>javax.annotation</groupId>
    <artifactId>jsr250-api</artifactId>
    <version>1.0</version>
</dependency>
```
#### 6.2.2.2 添加jsr250形式的服务类
```java
package com.wisely.prepost;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

@Service
public class JSR250WayService {
	@PostConstruct
	public void init(){
		System.out.println("init-method-annotation");
	}
	public JSR250WayService() {
		super();
		System.out.println("初始化构造函数-annotation");
	}
	@PreDestroy
	public void destroy(){
		System.out.println("destory-method-annotation");
	}
}

```
#### 6.2.2.3 测试
```java
package com.wisely.prepost;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =  new AnnotationConfigApplicationContext("com.wisely.prepost");
		context.close();

	}

}

```

输出结果

```java
初始化构造函数-annotation
init-method-annotation
destory-method-annotation

```
