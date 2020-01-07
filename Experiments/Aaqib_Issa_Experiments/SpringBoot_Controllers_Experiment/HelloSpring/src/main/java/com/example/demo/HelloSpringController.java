package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HelloSpringController {
	
	@RequestMapping("/")
	@ResponseBody
	public String start() {
		return "Welcome to my Page!";
	}
	
	@RequestMapping("/helloworld")
	@ResponseBody
	public String helloWorld() {
		return "Hello World!";
	}
	
	@RequestMapping("/helloaaqib")
	@ResponseBody
	public String helloAaqib() {
		return "Hello Aaqib!";
	}
	
	@RequestMapping("/helloisu")
	@ResponseBody
	public String helloISU() {
		return "Hello ISU!";
	}
	
	@RequestMapping("/helloeveryone")
	@ResponseBody
	public String helloEveryone() {
		return "Hello Everyone!";
	}

}
