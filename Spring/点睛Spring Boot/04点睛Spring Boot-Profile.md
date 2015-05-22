## 4.1 profile
- profile针对不同的环境使用不同的配置提供支持


- 全局profile配置使用`application-{profile}.properties`(如application-production.properties)


- 在**application.properties**`spring.profiles.active= production`



## 4.2 演示自定义全局配置(Profile-specific properties)

### 4.2.1 新建自定义全局配置

- `application-production.properties`

 - 放置在<<02点睛Spring Boot-外部配置>>中提及四个位置皆可;
 - 我放置在和`application.properties`同目录;  

```java
server.port=8888 # 默认端口为8080,在此修改为8888
```

### 4.2.2 修改`application.properties`指定profile

```java
spring.profiles.active=production
```

### 4.2.3 测试

此时程序以8080端口启动

```java
2015-05-20 11:35:42.908  INFO 9160 --- [main] 
s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8888 (http)
2015-05-20 11:35:42.911  INFO 9160 --- [main] 
com.wisely.demoboot.DemoBootApplication  : Started DemoBootApplication in 5.922 seconds (JVM running for 6.748)
```
