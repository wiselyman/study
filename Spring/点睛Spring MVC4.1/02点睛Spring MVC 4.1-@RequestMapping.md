## 2.1 @RequestMapping
- **@RequestMapping**是SpringMVC的核心注解,负责访问的url与调用方法之间的映射;
- **@RequestMapping**可以放在类和方法上;
 - @RequestMapping的属性produces属性控制response返回的形式;
 - @RequestMapping的属性method属性控制接受访问的类型,不写不做限制,本例为演示方便全部都是get请求;
- **@ResponseBody**(放在方法上或者返回值类型前)将方法参数放置在web body的body中(返回的不是页面而是你所控制的字符)
- **@RequestBody**(放在方法参数前)将方法参数放置在web request的body中(如提交一个json对象作为参数-在`03点睛Spring MVC 4.1-REST`演示)
- `produces`的内容是指定返回的媒体类型让浏览器识别
  - 如返回text/plain的话,chrome浏览器下network显示Response的`Content-Type:text/plain`;
  - 如返回application/json的话,chrome浏览器下network显示Response的`application/json`;
  - 因本节无页面,在`03点睛Spring MVC 4.1-REST`有只管的阐述和演示;
- 这节使用**@RequestMapping**演示常用映射场景




## 2.2 演示

- 传值对象

```
package com.wisely.web;

public class DemoObj {
	private Long id;
	private String name;

	public DemoObj(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}

```
- 控制器 `TestController`

```
package com.wisely.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //声明为控制器bean
@RequestMapping("/test")// 根地址为http://localhost:8080/testSpringMVC/test
public class TestController {
	//response媒体类型(MediaType)为text/plain,编码是utf-8
	@RequestMapping(produces = "text/plain;charset=UTF-8")
    //映射地址为http://localhost:8080/testSpringMVC/test
	@ResponseBody //此注解让返回值不是页面,也是将结果字符串直接返回
	public  String root(HttpServletRequest request){
		return "url:"+request.getRequestURL()+" 可以访问此方法";
	}

	@RequestMapping(value = "/add",produces = "text/plain;charset=UTF-8")
    //映射地址为http://localhost:8080/testSpringMVC/test/add
	@ResponseBody
	public   String add(HttpServletRequest request){
		return "url:"+request.getRequestURL()+" 可以访问此方法";
	}

	@RequestMapping(value = {"/remove","/delete"},produces = "text/plain;charset=UTF-8")
    //映射地址为http://.../test/remove(或http://.../test/delete)
	@ResponseBody
	public   String remove(HttpServletRequest request){
		return "url:"+request.getRequestURL()+" 可以访问此方法";
	}

	//获取request参数
	//获取路径参数
	@RequestMapping(value = "/get",produces = "text/plain;charset=UTF-8")
    //映射路径http://.../test/get?id=123
	@ResponseBody
	public String passRequestParam(@RequestParam Long id,HttpServletRequest request){
		System.out.println("id为"+id);
		return "url:"+request.getRequestURL()+" 可以访问此方法";

	}


	//获取路径参数
	@RequestMapping(value = "/{id}",produces = "text/plain;charset=UTF-8")
    //映射路径http://.../test/123
	@ResponseBody
	public String passPathVariable(@PathVariable Long id,HttpServletRequest request){
		System.out.println("id为"+id);
		return "url:"+request.getRequestURL()+" 可以访问此方法";

	}

	//获得对象
	@RequestMapping(value = "/pass",produces = "text/plain;charset=UTF-8")//映射路径http://.../test/pass?id=123&name=wyf
	@ResponseBody
	public String passObj(DemoObj obj,HttpServletRequest request){
		System.out.println("对象的id和名称分别为为："+obj.getId()+"/"+obj.getName());
		return "url:"+request.getRequestURL()+" 可以访问此方法";

	}

	//从json中获得对象
	@RequestMapping(value = "/json",produces = "text/plain;charset=UTF-8")//映射路径http://.../test/json
	@ResponseBody
	public String passJson(@RequestBody DemoObj obj,HttpServletRequest request){
		System.out.println("对象的id和名称分别为为："+obj.getId()+"/"+obj.getName());
		return "url:"+request.getRequestURL()+" 可以访问此方法";

	}



}

```
