package com.wisely.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class Demo2Service {
	@Autowired //注入Demo1Service,还可使用JavaEE的@Inject(JSR-330),@Resource(JSR-250)效果相同
	Demo1Service demo1Service;
	public String callDemo1SayHello(String word){
		return demo1Service.sayHello(word);
	}

}
