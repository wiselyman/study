## 15.1 TaskExecutor
- spring的**TaskExecutor**为在spring环境下进行并发的多线程编程提供了支持;
- 使用**ThreadPoolTaskExecutor**可实现一个基于线程池的**TaskExecutor**;
- 使用**@EnableAsync**开启异步任务支持;
- 使用**@Async**注解方法是异步方法;

## 15.2 示例
### 15.2.1 声明taskExecutor
```java
package com.wisely.task.executor;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class DemoConfig implements AsyncConfigurer{
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(5);
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setQueueCapacity(25);
		taskExecutor.initialize();
		return taskExecutor;
	}

	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return null;
	}

}


```

### 15.2.2 异步任务实现代码
```java
package com.wisely.task.executor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DemoAsyncTask {
	@Async
	public void executeAsyncTask(Integer i){
		System.out.println("执行异步任务:"+i);
	}

	@Async
	public void executeAsyncTaskPlus(Integer i){
		System.out.println("执行异步任务+1:"+(i+1);
	}
}

```

### 15.2.3 测试
```java
package com.wisely.task.executor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext("com.wisely.task.executor");
		DemoAsyncTask task = context.getBean(DemoAsyncTask.class);
		for(int i =0 ;i<10;i++){
			task.executeAsyncTask(i);
			task.executeAsyncTaskPlus(i);
		}
		context.close();

	}


}


```

输出结果(结果是并发执行而不是顺序执行的):
```java
执行异步任务+1:10
执行异步任务:6
执行异步任务:4
执行异步任务+1:7
执行异步任务:3
执行异步任务:1
执行异步任务:5
执行异步任务:7
执行异步任务+1:8
执行异步任务:8
执行异步任务+1:9
执行异步任务:9
执行异步任务+1:1
执行异步任务:0
执行异步任务+1:2
执行异步任务+1:3
执行异步任务:2
执行异步任务+1:4
执行异步任务+1:5
执行异步任务+1:6
```