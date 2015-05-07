package com.wisely.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration //声明是一个配置类
@PropertySource("com/wisely/javaconfig/test.properties")
public class DemoConfig {

	@Bean //声明是一个bean
	public DemoService demoBean(Environment environment){
		DemoService demoService = new DemoService();
		demoService.setWord(environment.getProperty("wisely.word"));
		return demoService;
	}
}
