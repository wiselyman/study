## 8.1 配置

- Spring MVC的配置是通过继承`WebMvcConfigurerAdapter`类并重载其方法实现的;
- 前几个教程已做了得配置包括
  - **01点睛Spring MVC 4.1-搭建环境** 配置viewResolver
  - **03点睛Spring MVC 4.1-REST** 静态资源映射
  - **04点睛Spring MVC 4.1-拦截器** 配置拦截器
  - **06点睛Spring MVC 4.1-文件上传** 配置multipartResolver
  - **07点睛Spring MVC4.1-ContentNegotiatingViewResolver** 配置ContentNegotiatingViewResolver

- 本节将演示如下配置
 - 默认

## 8.2 演示
### 8.2.1 配置路径匹配参数
- 在Spring MVC中路径参数如果带`.`的话,`.`后面的值将被忽略,本例演示配置`configurePathMatch`不忽略点后面的参数;

- `演示控制器`

```java
@RequestMapping("/configPath/{test}")
	public @ResponseBody String configPath(@PathVariable String test){
		return "request value:"+test;
	}

```
- 运行:访问http://localhost:8080/testSpringMVC/configPath/xx.yy

![](resources/8-1.jpg)

- 在继承`WebMvcConfigurerAdapter`的`DemoMVCConfig`类中重载`configurePathMatch`

```java
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
    	 configurer.setUseSuffixPatternMatch(false);
    }
```

- 再次运行:访问访问http://localhost:8080/testSpringMVC/configPath/xx.yy
-
![](resources/8-2.jpg)
