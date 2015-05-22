## 12.1 Aware
- 我们设计的准则是解耦,这就意味着我们不能对Spring的IoC容器有直接的依赖,但是我们还是想我们的bean能识别容器的资源;
- 使用aware能让我们在应用的任意位置获得spring容器的资源;
- 我们通过实现**aware**接口来识别spring容器的资源;
- Spring包含的aware有:
 - **BeanNameAware**
 - **BeanFactoryAware**
 - **ApplicationContextAware**
 - **MessageSourceAware**
 - **ApplicationEventPublisherAware**
 - **ResourceLoaderAware**
- 实现**ApplicationContextAware**接口,可识别所有的资源,但最好是你用到什么就使用什么;
- 这也就意味着我们就耦合到了spring框架上了,没有spring框架你的代码将无法执行;



## 12.2 示例
### 12.2.1 新建演示bean
```java
package com.wisely.aware;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
@Component
public class DemoBean implements BeanNameAware,ResourceLoaderAware{
	private String name;
	private ResourceLoader loader;

	//BeanNameAware接口的方法
	public void setBeanName(String beanName) {
		this.name = beanName;

	}

	//ResourceLoaderAware接口的的方法
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.loader = resourceLoader;

	}

	public String getName() {
		return name;
	}


	public ResourceLoader getLoader() {
		return loader;
	}

}

```
### 12.2.2 新建演示用文件info.txt
```java
jhkljhlkjhlkj
111111111111
```
### 12.2.3 测试
```java
package com.wisely.aware;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class Main {

	public static void main(String[] args) throws IOException {
		AnnotationConfigApplicationContext context =
        		new AnnotationConfigApplicationContext("com.wisely.aware");
		DemoBean db = context.getBean(DemoBean.class);
		System.out.println(db.getName());
		ResourceLoader rl = db.getLoader();
		Resource r = rl.getResource("classpath:com/wisely/aware/info.txt");
		System.out.println(IOUtils.toString(r.getInputStream()));
		context.close();
	}

}

```

输出结果

```java
demoBean
jhkljhlkjhlkj
111111111111
```