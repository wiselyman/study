## 10.1 Application Event
- Spring使用Application Event给bean之间的消息通讯提供了手段
- 应按照如下部分实现bean之间的消息通讯
 - 继承ApplicationEvent类实现自己的事件
 - 实现继承ApplicationListener接口实现监听事件
 - 使用ApplicationContext发布消息

## 10.2示例
示例中的通讯两个bean分别为DemoListener和Main

### 10.2.1 编写自定义的ApplicationContext
```java
package com.wisely.event;

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
### 10.2.2 编写实现ApplicationListener的类
```java
package com.wisely.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
@Component
public class DemoListener implements ApplicationListener<DemoEvent> {

	public void onApplicationEvent(DemoEvent event) {
			String msg = ((DemoEvent) event).getMsg();
			System.out.println("我监听到了pulisher发布的message为:"+msg);

	}


}

```
### 10.2.3 测试
```java
package com.wisely.event;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
@Component
public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
        		new AnnotationConfigApplicationContext("com.wisely.event");
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

```java
我监听到了pulisher发布的message为:22
```