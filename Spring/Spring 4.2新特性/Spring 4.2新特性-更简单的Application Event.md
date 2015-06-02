## 1.1 Application Event

- Spring 4.1的写法请参考[10点睛Spring4.1-Application Event](http://wiselyman.iteye.com/blog/2212013)

- 请对比[10点睛Spring4.1-Application Event](http://wiselyman.iteye.com/blog/2212013)

- 使用一个`@EventListener`取代了实现`ApplicationListener`接口,使耦合度降低;

## 1.2 示例

- 包依赖

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.wisely</groupId>
  <artifactId>spring4-2</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>spring4-2</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

 <dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>4.2.0.RC1</version>
    </dependency>
</dependencies><repositories>
    <repository>
        <id>spring-milestones</id>
        <name>Spring Milestones</name>
        <url>http://repo.spring.io/milestone</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
</project>

```

- 编写自定义的Application Event

```java
package com.wisely.spring4_2.event;

import org.springframework.context.ApplicationEvent;

public class DemoEvent extends ApplicationEvent{
    private static final long serialVersionUID = 1L;
    private String msg;

    public DemoEvent(Object source,String msg) {
        super(source);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
```


- 编写监听类

```java
package com.wisely.spring4_2.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DemoListener {
	@EventListener //注意此处
	public void handleDemoEvent(DemoEvent event){
		System.out.println("我监听到了pulisher发布的message为:"+event.getMsg());
		
	}

}

```

- 测试

```java
package com.wisely.spring4_2.event;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
@Component
public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = 
                  new AnnotationConfigApplicationContext("com.wisely.spring4_2.event");
		Main main =context.getBean(Main.class);
        main.pulish(context);
        context.close();
	}
 public void pulish(AnnotationConfigApplicationContext context){
        context.publishEvent(new DemoEvent(this, "22"));
    }
}

```

输出结果

```
我监听到了pulisher发布的message为:22
```