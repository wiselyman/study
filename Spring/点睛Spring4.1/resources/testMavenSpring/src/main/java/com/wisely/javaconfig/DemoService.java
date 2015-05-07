package com.wisely.javaconfig;

public class DemoService {
	private String word;
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String sayHello(){
		return "Hello "+this.word;
	}
	
}
