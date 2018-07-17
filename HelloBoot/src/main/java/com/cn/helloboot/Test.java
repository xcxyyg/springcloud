package com.cn.helloboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
	
	private static final Logger logger = LoggerFactory.getLogger(Test.class);

	@Autowired
	private ConnectionSettings cf;
	
	@RequestMapping("/aaa")
	String home() {
		logger.info("哈哈哈哈哈哈哈");
		logger.debug("嘿嘿嘿嘿嘿嘿嘿嘿嘿");
		logger.error("嘻嘻嘻嘻嘻嘻嘻嘻嘻");
		logger.warn("嘻嘻嘻嘻嘻嘻嘻嘻嘻");
		System.out.println("address = "+cf.getAddress());
		System.out.println("port = "+cf.getPort());
		return "Hello World!";
	}
}
