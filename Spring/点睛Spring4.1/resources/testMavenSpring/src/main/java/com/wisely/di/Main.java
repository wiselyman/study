package com.wisely.di;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

	public static void main(String[] args) {
		//设定此包下的类被注册成spring的bean,包含@Configuration,@Component,@Service,@Repository,@Controller
		AnnotationConfigApplicationContext context =  new AnnotationConfigApplicationContext("com.wisely.di");
		Demo2Service demo2Service = context.getBean(Demo2Service.class);
		System.out.println(demo2Service.callDemo1SayHello("World"));
		context.close();
	}

}
