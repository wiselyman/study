## 5.1 ReloadableResourceBundleMessageSource
- 使用**ReloadableResourceBundleMessageSource**可获得不同语言的配置
- 此处是全局配置,适合用@Bean声明

## 5.2 示例

### 5.2.1 新建英文messagesmessages_en_US.properties
```java
wisely.name = wyf
wisely.age = 32
```

### 5.2.2 新建中文messagesmessages_zh_CN.properties
```java
wisely.name = \u6C6A\u4E91\u98DE
wisely.age = 3-10-2
```

### 5.2.3 配置ReloadableResourceBundleMessageSource
```java
package com.wisely.i18n;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class I18NConfig {
	@Bean
	public static ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource= new ReloadableResourceBundleMessageSource();
		String[] resources = {"classpath:com/wisely/i18n/messages"};
		messageSource.setBasenames(resources);
		messageSource.setCacheSeconds(1);
		return messageSource;
	}

}
```

### 5.2.4 测试
```java
package com.wisely.i18n;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
@Component
public class Main {
	@Autowired
	MessageSource messageSource;

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
        		new AnnotationConfigApplicationContext("com.wisely.i18n");
		String nameEn = context.getMessage("wisely.name",null, Locale.US);
		String nameCn = context.getMessage("wisely.name",null, Locale.CHINA);
		System.out.println("nameEN="+nameEn);
		System.out.println("nameCN="+nameCn);

		Main main = context.getBean(Main.class);
		main.showAgeInfo(context);

		context.close();
	}

	public void showAgeInfo(AnnotationConfigApplicationContext context){
		String ageEn = context.getMessage("wisely.age",null, Locale.US);
		String ageCn = context.getMessage("wisely.age",null, Locale.CHINA);
		System.out.println("nameEN="+ageEn);
		System.out.println("nameCN="+ageCn);
	}
}

```
输出结果
```java
nameEN=wyf
nameCN=汪云飞
nameEN=32
nameCN=3-10-2
```