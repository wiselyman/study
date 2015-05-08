## 4.1 Resource
- spring用来调用外部资源数据的方式
- 支持调用文件或者是网址
- 在系统中调用properties文件可参考<<02点睛Spring4.1-Java Config>>中结合**@PropertySource**和E**nvironment**来使用

## 4.2 示例

### 4.2.1 新增commons-io到maven依赖
需使用commons-io的IOUtils工具类将InputStream转换成String
在pom.xml的<dependencies>中添加如下
```
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.3</version>
</dependency>

```

### 4.2.2 新建测试用info.txt
```
sadfasdfasdfasdfasdfsad
sadfasdfasdfasdfasdfsad
sadfasdfasdfasdfasdfsad
```

### 4.2.3 测试

```
package com.wisely.resource;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Configuration
public class Main {

	public static void main(String[] args) throws IOException {
		AnnotationConfigApplicationContext context =  new AnnotationConfigApplicationContext("com.wisely.resource");
		//classpath: spring的一个模拟协议,类似于http：
		Resource file = context.getResource("classpath:com/wisely/resource/info.txt");
		System.out.println(IOUtils.toString(file.getInputStream()));

		Resource url = (UrlResource) context.getResource("http://www.baidu.com");
		System.out.println(IOUtils.toString(url.getInputStream()));
		context.close();
	}

}


```

输出结果
```
sadfasdfasdfasdfasdfsad
sadfasdfasdfasdfasdfsad
sadfasdfasdfasdfasdfsad
<!DOCTYPE html><!--STATUS OK--><html><head>
.......
```
