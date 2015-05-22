## 14.1 Scripting脚本编程
- 脚本语言和java这类静态的语言的主要区别是:脚本语言无需编译,源码直接可运行;
- 如果我们经常需要修改的某些代码,每一次我们至少要进行编译,打包,重新部署的操作,步骤相当麻烦;
- 如果我们的应用不允许重启,这在现实的情况中也是很常见的;
- 在spring中使用脚本编程给上述的应用场景提供了解决方案,即动态加载bean;
- spring支持脚本语言包含**JRuby,Groovy,BeanShell**;
- 本例以spring主推的Groovy语言作为示例;
- 动态加载bean目前暂不支持java config(应该在spring4.2版本支持,参见:[Add support for dynamic languages refreshable beans in @Configuration classes](https://jira.spring.io/browse/SPR-12300)),暂且使用xml配置()

## 14.2 示例

### 14.2.1 增加groovy语言支持包到maven
```xml
<dependency>
    <groupId>org.codehaus.groovy</groupId>
    <artifactId>groovy-all</artifactId>
    <version>2.4.3</version>
</dependency>
```
### 14.2.2 演示接口
```java
package com.wisely.script;

public interface DemoService {
	public String sayHello();
}

```

### 14.2.3 使用groovy作为接口实现
```java
import com.wisely.script.DemoService
class DemoServiceImpl implements DemoService{
	def msg
	String sayHello(){
		return 'hello'+msg+' ok' //第一次打印后修改成为'hello'+msg+' not ok'
	}
}
```

### 14.2.4 使用xml配置bean
- script-source指定groovy源码地址
- refresh-check-delay指定刷新时间
- lang:property可注入值到groovy bean,包含普通值或者spring的bean

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:lang="http://www.springframework.org/schema/lang"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/lang http://www.springframework.org/schema/lang/spring-lang-4.1.xsd">
<lang:groovy id="demoService"
	script-source="com/wisely/script/DemoService.groovy"
	refresh-check-delay="5000">
	<lang:property name="msg" value="1234"/>
</lang:groovy>

</beans>

```

### 14.2.5 测试

```java
package com.wisely.script;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
@Configuration
@ImportResource("classpath:com/wisely/script/script.xml")//加载groovybean的配置文件
public class Main {
	public static void main(String[] args) throws InterruptedException {
		AnnotationConfigApplicationContext context =
        		new AnnotationConfigApplicationContext("com.wisely.script");
		DemoService ds = context.getBean(DemoService.class);

		System.out.println(ds.sayHello());
		Thread.sleep(10000);
		System.out.println(ds.sayHello());
		context.close();
	}

}
```

**说明**
先执行sayHello输出groovy bean的执行结果,此时修改groovy bean的sayHello的内容,5000毫秒后会加载新的bean的内容,我们等10秒,等待新的bean被加载,然后输出新的sayHello

输出结果:

```java
hello 1234 ok
hello 1234 not ok

```


