## 17.1 @Conditional
- @Conditional为按照条件配置spring的bean提供了支持,即满足某种条件下,怎么配置对应的bean;
- 应用场景
  - 当某一个jar包在classpath中的时候,配置某几个bean;
  - 当某一个bean配置好后,会自动配置一个特定的bean;
  - 当某种环境变量被设置后,创建某个bean;
- @Conditional为敏捷开发所提倡的原则"习惯优于配置"提供了支持;
- @Conditional是Spring Boot快速开发框架实现"习惯优于配置"的核心技术;

## 17.2 示例
演示在windows和linux系统下,初始化不同的bean,windows和linux作为判断条件;

### 17.2.1 构造判断条件
条件的构造需要类实现**Condition**接口,并实现**matches**方法
- WindowsCondition

```java
package com.wisely.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class WindowsCondition implements Condition {

	public boolean matches(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		return context.getEnvironment().getProperty("os.name").contains("Windows");
	}

}

```
- LinuxCondition

```java
package com.wisely.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class LinuxCondition implements Condition {

	public boolean matches(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		return context.getEnvironment().getProperty("os.name").contains("Linux");
	}

}

```

### 17.2.2 编写不同条件下需要的bean

- 接口

```java
package com.wisely.conditional;


public interface CommandService {

	public String showListCommand();
}

```

- WindowsCommnadService

```java
package com.wisely.conditional;

public class WindowsCommnadService implements CommandService {

	public String showListCommand() {
		return "dir";
	}

}

```

- LinuxCommandService

```java
package com.wisely.conditional;

public class LinuxCommandService implements CommandService {

	public String showListCommand() {
		return "ls";
	}

}

```

### 17.2.3 编写配置类

```java
package com.wisely.conditional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoConfig {
	@Bean
	@Conditional(WindowsCondition.class)
	public CommandService commandService() {
		return new WindowsCommnadService();
	}

	@Bean
	@Conditional(LinuxCondition.class)
	public CommandService linuxEmailerService() {
		return new LinuxCommandService();
	}

}

```

### 17.2.4 测试

- windows下

```java
package com.wisely.conditional;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
        		new AnnotationConfigApplicationContext("com.wisely.conditional");
		CommandService cs = context.getBean(CommandService.class);
		System.out.println(cs.showListCommand());
		context.close();
	}
}

```

输出结果

```java
dir
```

- Linux下(本例没有切换到linux,直接修改os.name为Linux)

```java
package com.wisely.conditional;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		System.setProperty("os.name", "Linux");
		AnnotationConfigApplicationContext context =  new AnnotationConfigApplicationContext("com.wisely.conditional");
		CommandService cs = context.getBean(CommandService.class);
		System.out.println(cs.showListCommand());
		context.close();

	}
}

```

输出结果
```java
ls
```
