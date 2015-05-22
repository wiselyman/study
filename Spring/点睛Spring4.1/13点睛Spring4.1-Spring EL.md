## 13.1 Spring EL
- Spring EL-Spring表达式语言,支持在xml和注解中使用表达式,类似jsp的EL表达式语言;
- 本教程关注于在注解中使用Spring EL;
- Spring EL包含很多类型的表达式,本教程关注常用的注入
 - 获得系统属性
 - 注入表达式
 - 注入文件
 - 注入其他bean或者其属性
 - 注入properties文件属性
 - 注入普通字符

## 13.2 示例
### 13.2.1 编写Spring EL演示类
```java
@Configuration
@PropertySource("classpath:com/wisely/springel/test.properties")
public class DemoService {
	@Value("#{systemProperties}")
	private Properties systemProperties;

	@Value("#{systemProperties['os.name']}")
	private String osName;

	@Value("#{ T(java.lang.Math).random() * 100.0 }")
	private double randomNumber;

	@Value("classpath:com/wisely/springel/info.txt")
	private Resource info;

	@Value("#{demoBean.another}")
	private String fromAnother;
	//注意注入properties使用$而不是#,且需要声明propertyConfigure,在下面的@Bean
	@Value("${wisely.name}")
	private String myName;

	@Value("I love iteye and github")
	private String normal;
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigure() {
		return new PropertySourcesPlaceholderConfigurer();
	}

//getter

}

```

### 13.2.2 编写java类测试注入其他bean的属性
```java
package com.wisely.springel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class DemoBean {
    @Value("其他类的属性")
	private String another;

	public String getAnother() {
		return another;
	}

	public void setAnother(String another) {
		this.another = another;
	}

}

```

### 13.2.3 新建测试txt和properties
```java
1234
```

```java
wisely.name = wyf
```

### 13.2.3 测试
```java
package com.wisely.springel;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
        		new AnnotationConfigApplicationContext("com.wisely.springel");
		DemoService ds = context.getBean(DemoService.class);
		System.out.println("注入操作系统属性："+ds.getSystemProperties());
		System.out.println("注入操作系统名称: "+ds.getOsName());
		System.out.println("注入随机数: "+ds.getRandomNumber());
		System.out.println("注入文件 "+ds.getInfo().getFilename());
		System.out.println("注入其他bean属性: "+ds.getFromAnother());
		System.out.println("注入properties文件的属性: "+ds.getMyName());
		System.out.println("注入普通字符 "+ds.getNormal());
		context.close();

	}

}

```

输出结果:

```java
注入操作系统属性：{java.runtime.name=Java(TM) SE Runtime Environment......}
注入操作系统名称: Windows 8.1
注入随机数: 15.342237263367908
注入文件 info.txt
注入其他bean属性: 其他类的属性
注入properties文件的属性: wyf
注入普通字符 I love iteye and github
```