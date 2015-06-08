## 10.1 全局异常处理

- 使用`@ControllerAdvice`注解来实现全局异常处理;
- 使用`@ControllerAdvice`的属性缩小处理范围


## 10.2 演示

- 演示控制器

```java
package com.wisely.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdviceController {
	@RequestMapping("/advice")
	public String getSomething(){
		throw new IllegalArgumentException("不好意思,参数错了");
	}

}

```

- `@ControllerAdvice`配置

```java
package com.wisely.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(value=Exception.class)
	public ModelAndView exception(Exception exception,WebRequest request){
		ModelAndView modelAndView = new ModelAndView("error");//error页面
		modelAndView.addObject("errorMessage",exception.getMessage());
		return modelAndView;

	}
}

```

- 错误展示页面`webapp/WEB-INF/views/error.jsp`

```java
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
${errorMessage}
</body>
</html>
```

- 访问`http://localhost:8080/testSpringMVC/advice`

- 页面显示`不好意思,参数错了`