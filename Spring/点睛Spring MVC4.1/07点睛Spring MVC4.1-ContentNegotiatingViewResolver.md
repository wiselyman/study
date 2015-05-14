## 7.1 ContentNegotiatingViewResolver
- ContentNegotiatingViewResolver支持在Spring MVC下输出不同的格式;
- ContentNegotiatingViewResolver是ViewResolver的一个实现;
- ContentNegotiatingViewResolver使用request的媒体类型,根据扩展名选择不同的view输出不同的格式;
- ContentNegotiatingViewResolver不是自己处理view,而是代理给不同的ViewResolver来处理不同的view(json,xml,pdf.xls,html...)

## 7.2 示例

- 依赖
 - json依赖`03点睛Spring MVC 4.1-REST`已添加
 - xml

 ```
 <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-oxm</artifactId>
    <version>${spring-framework.version}</version>
</dependency>
 ```

 - pdf

 ```
 <dependency>
	<groupId>com.lowagie</groupId>
	<artifactId>itext</artifactId>
	<version>4.2.1</version>
</dependency>

 ```
 - xls

 ```
 <dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>3.10-beta2</version>
</dependency>
 ```