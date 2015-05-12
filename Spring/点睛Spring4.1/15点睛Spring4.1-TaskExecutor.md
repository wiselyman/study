## 15.1 TaskExecutor
- spring的**TaskExecutor**为在spring环境下进行并发的多线程编程提供了支持;
- 使用**ThreadPoolTaskExecutor**可实现一个基于线程池的**TaskExecutor**;

## 15.2 示例
### 15.2.1 声明taskExecutor
```
package com.wisely.task.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class DemoConfig {
	@Bean
	public TaskExecutor taskExecutor(){
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(5);
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setQueueCapacity(25);
		return taskExecutor;
	}

}

```

### 15.2.2 测试
```
package com.wisely.task.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class Main {
	@Autowired
	private TaskExecutor taskExecutor; //注入taskExecutor

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext("com.wisely.task.executor");
		Main main = context.getBean(Main.class);
		main.printMessages();
		context.close();

	}

	public void printMessages(){
		for(int i = 0; i < 10; i++) {
            taskExecutor.execute(new MessagePrinterTask("Message" + i));
        }
	}

	//定义线程任务
	private class MessagePrinterTask implements Runnable {

	        private String message;

	        public MessagePrinterTask(String message) {
	            this.message = message;
	        }

	        public void run() {
	            System.out.println(message);
	        }

	    }

}

```

输出结果(结果是并发执行而不是顺序执行的):
```
Message0
Message1
Message2
Message3
Message5
Message6
Message7
Message8
Message9
Message4
```