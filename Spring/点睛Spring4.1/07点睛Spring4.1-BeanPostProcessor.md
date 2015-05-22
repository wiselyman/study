## 7.1 BeanPostProcessor
- spring通过**BeanPostProcessor**接口可以对所有bean或者指定的某些bean的初始化前后对bean的检查或者修改提供支持;
- 使用**postProcessBeforeInitialization**和**postProcessAfterInitialization**对bean进行操作;
- **postProcessBeforeInitialization**和**postProcessAfterInitialization**返回值是bean;

## 7.2 示例
### 7.2.1 处理全部bean
#### 7.2.1.1 新建两个测试用的bean
```java
package com.wisely.beanpostprocessor;

import org.springframework.stereotype.Service;

@Service
public class DemoNormal1Service {

}

```

```java
package com.wisely.beanpostprocessor;

import org.springframework.stereotype.Service;

@Service
public class DemoNormal2Service {

}

```

#### 7.2.1.2 编写处理所有bean的BeanPostProcessor
```java
package com.wisely.beanpostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class DemoAllBeanPostProcessor implements BeanPostProcessor{

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		System.out.println("在 DemoAllBeanPostProcessor的"
        +postProcessBeforeInitialization方法里处理bean: " + beanName
        +" bean的类型为:"+bean.getClass());
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		System.out.println("在 DemoAllBeanPostProcessor的"+
        postProcessAfterInitialization方法里处理bean: " + beanName
        +" bean的类型为:"+bean.getClass());
		return bean;
	}

}

```
#### 7.2.1.3 测试
```java
package com.wisely.beanpostprocessor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =  
                 new AnnotationConfigApplicationContext("com.wisely.beanpostprocessor");
		context.close();

	}

}
```
输出结果为:
```java
在 DemoAllBeanPostProcessor的postProcessBeforeInitialization方法里处理bean:
demoNormal1Service bean的类型为:class com.wisely.beanpostprocessor.DemoNormal1Service
在 DemoAllBeanPostProcessor的postProcessAfterInitialization方法里处理bean:
demoNormal1Service bean的类型为:class com.wisely.beanpostprocessor.DemoNormal1Service
在 DemoAllBeanPostProcessor的postProcessBeforeInitialization方法里处理bean:
demoNormal2Service bean的类型为:class com.wisely.beanpostprocessor.DemoNormal2Service
在 DemoAllBeanPostProcessor的postProcessAfterInitialization方法里处理bean:
demoNormal2Service bean的类型为:class com.wisely.beanpostprocessor.DemoNormal2Service
```


### 7.2.2 处理指定的bean

#### 7.2.2.2 新建指定处理的bean
已经给os和num属性赋值,将在BeanPostProcessor的实现类对类的属性进行修改

```java
package com.wisely.beanpostprocessor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DemoSelectedService {
	@Value("#{systemProperties['os.name']}")
	private String os;
	@Value("123")
	private Long num;

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}


}
```

#### 7.2.2.3 编写指定bean的BeanPostProcessor
```java
package com.wisely.beanpostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
@Component
public class DemoSelectedBeanPostProcessor implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		if(bean instanceof  DemoSelectedService){
			((DemoSelectedService) bean).setOs("Linux");
			System.out.println("在DemoSelectedBeanPostProcessor的"
            +"postProcessBeforeInitialization中将os从windows修改成了Linux" );
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if(bean instanceof  DemoSelectedService){
			((DemoSelectedService) bean).setOs("Linux");
			System.out.println("在DemoSelectedBeanPostProcessor的"+
            "postProcessBeforeInitialization中将num从123修改成了456" );
		}
		return bean;
	}

}

```
#### 7.2.2.4 测试
```java
package com.wisely.beanpostprocessor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
                 new  AnnotationConfigApplicationContext("com.wisely.beanpostprocessor");
		DemoSelectedService dss = context.getBean(DemoSelectedService.class);
		System.out.println("os确实被修改成了"+dss.getOs());
		System.out.println("num确实被修改成了"+dss.getNum());
		context.close();

	}

}


```

输出结果

```java
在DemoSelectedBeanPostProcessor的postProcessBeforeInitialization中将os从windows修改成了Linux
在DemoSelectedBeanPostProcessor的postProcessBeforeInitialization中将num从123修改成了456
os确实被修改成了Linux
num确实被修改成了123
```