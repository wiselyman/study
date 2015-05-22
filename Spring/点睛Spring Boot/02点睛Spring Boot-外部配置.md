## 2.1 外部配置
- Spring boot使用外部配置可使相同的代码在不同的环境下运行(如开发或生产环境的不同配置);

- 可使用`properties`文件,`YAML`文件,环境变量以及命令行参数作为外部配置;

- 可使用**@Value**注入或使用**Environment**获得;

- Spring boot使用一个全局的配置的**application.properties**或**application.yml**

- 至于使用Properties文件还是YAML文件配置看个人喜好,本人习惯于使用properties文件


- application.properties或application.yml主要覆盖配置默认的自动配置
 - 修改程序端口号为80`server.port=80`,默认为8080;
 - 配置数据库

  `properties`方式:  

  ```java
  spring.datasource.driver-class-name=
    spring.datasource.url=
    spring.datasource.username=
    spring.datasource.password=
  ```

  `YAML`方式:  

  ```java
  spring: 
      datasource:
          driver-class-name:
          url:
          username:
          password:

  ```
 - 完整的配置请查看`<<spring boot reference>>`的`Part X.Appendices`

- application.properties或application.yml放置如下位置可生效
 - 根包下(com.wisely.demoboot)
 - 根包下的config目录(com.wisely.demoboot.config)
 - classpath根目录下(src/main或src/resources)
 - classpath的config目录下(src/main/config或src/resources/config)
 - 可使用`spring.config.location=`指定系统配置文件位置(包括修改名称);

## 2.2 演示自定义配置文件

- 本例的author.proerties内容可直接放置在application.properties中
 - 此时无需`locations = {"classpath:config/author.properties"}`
 
- 本例的reader.yml内容可直接放置在application.yml中
 - 此时无需`locations = {"classpath:config/reader.yml"}`

### 2.2.1 自定义properties配置文件
- `src/resources/config/author.properties`  
```java
author.name=wyf
author.age=32
```
- 类型安全配置类

```java
package com.wisely.demoboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "author",locations = {"classpath:config/author.properties"})
public class AuthorSettings {
    private String name;
    private Long age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}

```
### 2.2.2 自定义YAML配置文件
- `src/resources/config/reader.yml`  

```java
	reader:
    	name: phy
    	age: 27

```

- 类型安全配置类

```java
package com.wisely.demoboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "reader",locations = {"classpath:config/reader.yml"})
public class ReaderSettings {
    private String name;
    private Long age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}

```

### 2.2.3 开启配置支持

使用**@EnableConfigurationProperties**注解开启

```java
package com.wisely.demoboot;

import com.wisely.demoboot.config.AuthorSettings;
import com.wisely.demoboot.config.ReaderSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AuthorSettings.class, ReaderSettings.class})
public class DemoBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoBootApplication.class)
                .showBanner(false)
                .run(args);
    }
}

```

### 2.2.4 测试
```java
package com.wisely.demoboot;

import com.wisely.demoboot.config.AuthorSettings;
import com.wisely.demoboot.config.ReaderSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PropertySource("classpath:config/author.properties")
public class DemoController {
    @Autowired
    AuthorSettings authorSettings;
    @Autowired
    ReaderSettings readerSettings;
    @RequestMapping("/")
    public String response(){
        return authorSettings.getName()+"/"+authorSettings.getAge()+
        "/"+readerSettings.getName()+"/"+readerSettings.getAge();
    }
}
```
页面输出结果:
`wyf/32/phy/27`

