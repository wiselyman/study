## 16.1 TaskScheduler

- 提供对计划任务提供支持;
- 使用**@EnableScheduling**开启计划任务支持
- 使用**@Scheduled**来注解计划任务的方法;

## 16.2 示例

演示后台间断执行任务和定时计划任务

### 16.2.1 计划任务的配置

```java
@Configuration
@EnableScheduling
public class DemoConfig {

}

```

### 16.2.2 计划配置任务类

```java
package com.wisely.task.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DemoScheduledTask {

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  @Scheduled(fixedRate = 5000) //每五秒执行一次
  public void reportCurrentTime() {
	   System.out.println("每隔五秒执行一次 " + dateFormat.format(new Date()));
   }

  @Scheduled(cron = "0 22 11 ? * *"  ) //每天上午11点22执行
  public void fixTimeExecution(){
	  System.out.println("在指定时间 " + dateFormat.format(new Date())+"执行");
  }



}

```

### 16.2.3 测试

```java
package com.wisely.task.scheduler;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext("com.wisely.task.scheduler");

	}

}

```
输出结果

```java
每隔五秒执行一次 11:21:42
每隔五秒执行一次 11:21:47
每隔五秒执行一次 11:21:52
每隔五秒执行一次 11:21:57
在指定时间 11:22:00执行
每隔五秒执行一次 11:22:02
```