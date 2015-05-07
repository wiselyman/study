package com.wisely.javaconfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		//设定此包下的类被注册成spring的bean,包含@Configuration,@Component,@Service,@Repository,@Controller
		AnnotationConfigApplicationContext context =  new AnnotationConfigApplicationContext("com.wisely.javaconfig");
		DemoService demoService = context.getBean(DemoService.class);
		System.out.println(demoService.sayHello());
		context.close();
	}

}
