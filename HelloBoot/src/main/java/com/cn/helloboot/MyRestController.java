package com.cn.helloboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/users")
public class MyRestController {
	private static final Logger logger = LoggerFactory.getLogger(MyRestController.class);
	
	@RequestMapping(value="/{user}", method=RequestMethod.GET)
	public void getUser(@PathVariable Long user) {
		logger.info("/{user}=" + user);
	}

	@RequestMapping(value="/{user}/customers", method=RequestMethod.GET)
	public void getUserCustomers(@PathVariable Long user) {
		logger.info("/{user}/customers=" + user);
	}
	
	@RequestMapping(value="/{user}", method=RequestMethod.DELETE)
	public void deleteUser(@PathVariable Long user) {
		logger.info("/{user}/customers=" + user);
	}
}
