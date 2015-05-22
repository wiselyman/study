## 11.1 Propert Editor
- property editor是JavaBeans API的一项特性,用来字符和属性值之间的互相转换(如`2014-03-02`和`Date`类型的互相转换)
- spring内置了**CustomDateEditor, CustomNumberEditor, ClassEditor, FileEditor, LocaleEditor, StringArrayPropertyEditor**
- 除了内置的property editor,如需自己定制额外的复杂情况继承JavaBeans API的**PropertyEditorSupport**类

## 11.2 示例

### 11.2.1 使用Spring内置的Editor
#### 11.2.1.1 编写演示bean
```java
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class DemoBean {
	@Value("2014/02/03")
	private Date demoDate;

	public Date getDemoDate() {
		return demoDate;
	}

	public void setDemoDate(Date demoDate) {
		this.demoDate = demoDate;
	}


}

```
#### 11.2.1.2 编写配置
```java
package com.wisely.propertyeditor;

import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoConfig {
	@Bean
	public CustomDateEditor dateEditor(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return new CustomDateEditor(dateFormat, true);
	}
}

```
#### 11.2.1.3 测试
```java
package com.wisely.propertyeditor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
        		new AnnotationConfigApplicationContext("com.wisely.propertyeditor");
		DemoBean demoBean = context.getBean(DemoBean.class);
		System.out.println(demoBean.getDemoDate());
		context.close();
	}
}

```
输出结果
```java
Mon Feb 03 00:00:00 CST 2014
```

### 11.2.2 使用PropertyEditorSupport

#### 11.2.2.1 编写需要和字符转换的javabean
此为传值对象,不需要声明称spring的bean
```java
package com.wisely.propertyeditor;

public class DemoBean2 {
	private String name;
	private String address;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}

```

#### 11.2.2.2 在DemoBean中注入该bean
```java
package com.wisely.propertyeditor;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class DemoBean {

	@Value("汪云飞-合肥")
	private DemoBean2 demoBean2;


	public DemoBean2 getDemoBean2() {
		return demoBean2;
	}

	public void setDemoBean2(DemoBean2 demoBean2) {
		this.demoBean2 = demoBean2;
	}

}

```

#### 11.2.2.3 实现自定义的Property Editor
```java
package com.wisely.propertyeditor;

import java.beans.PropertyEditorSupport;

public class DemoPropertyEditor extends PropertyEditorSupport{

	@Override
	public String getAsText() {
		DemoBean2 bean2 =(DemoBean2) getValue();
		return bean2.getClass().getName() + "," + bean2.getName() 
                                     + "," + bean2.getAddress();
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String[] parts = text.split("-");
		try{
			DemoBean2 bean2 = new DemoBean2();
			bean2.setName(parts[0]);
			bean2.setAddress(parts[1]);
			setValue(bean2);
		}catch(Exception e){
			throw new IllegalArgumentException(e);
		}

	}

}

```

#### 11.2.2.4 配置editorConfigurer
```java
package com.wisely.propertyeditor;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Demo2Config {
	@Bean
	public CustomEditorConfigurer editorConfigurer(){
		CustomEditorConfigurer editorConfigurer = new CustomEditorConfigurer();
		Map<Class<?>, Class<? extends PropertyEditor>> customEditors =
        		new HashMap<Class<?>, Class<? extends PropertyEditor>>();
		customEditors.put(DemoBean2.class, DemoPropertyEditor.class);
		editorConfigurer.setCustomEditors(customEditors);
		return editorConfigurer;
	}

}

```

#### 11.2.2.5 测试
```java
package com.wisely.propertyeditor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
        		new AnnotationConfigApplicationContext("com.wisely.propertyeditor");
		DemoBean demoBean = context.getBean(DemoBean.class);
		System.out.println(demoBean.getDemoBean2().getName()+"///"
                       +demoBean.getDemoBean2().getAddress());
		context.close();
	}
}

```
输出结果
```java
汪云飞///合肥
```




