## 3.1 scope
- scope描述spring容器是怎么样新建类的实例的(bean);
- 在spring中默认的scope是singleton,这意味着无论你在程序中多少地方使用这个bean,它们都共享唯一个实例;
- spring内置的scope有如下几个:
  - singleton:一个spring容器中只有一个bean的实例;
  - prototype:每次调用新建一个bean的实例;
  - request:web项目中,每一个http request,新建一个bean实例;
  - session:web项目中,每一个http session,新建一个bean实例;
  - globalSession:这个只在portal应用中有用,每一个global http session,新建一个bean实例

## 3.2 演示

### 3.2.1 新建scope为singleton的类
```java
package com.wisely.scope;

import org.springframework.stereotype.Service;

@Service//默认scope为singleton
public class DemoSingletonService {

}

```

### 3.2.2 新建scope为prototype的类
```java
package com.wisely.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class DemoPrototypeService {

}

```

### 3.2.3 测试
```java
package com.wisely.scope;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
        					new AnnotationConfigApplicationContext("com.wisely.scope");
		//分别从spring容器里获得两次实例，为singleton时共享一个实例，用prototype是又新建了一个实例
		DemoSingletonService s1 = context.getBean(DemoSingletonService.class);
		DemoSingletonService s2 = context.getBean(DemoSingletonService.class);

		DemoPrototypeService p1 = context.getBean(DemoPrototypeService.class);
		DemoPrototypeService p2 = context.getBean(DemoPrototypeService.class);

		System.out.println("s1与s2是否相等："+s1.equals(s2));
		System.out.println("p1与p2是否相等："+p1.equals(p2));
		context.close();
	}

}

```

输出
```java
s1与s2是否相等：true
p1与p2是否相等：false
```
