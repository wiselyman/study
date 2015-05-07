package com.wisely.di;

import org.springframework.stereotype.Service;

@Service//写为@Component,@Controller,@Repository效果相同,视具体情况使用
public class Demo1Service {
	
	public String sayHello(String word ){
		return "Hello "+word;
	}
}
