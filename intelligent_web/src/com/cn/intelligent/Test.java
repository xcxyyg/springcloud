package com.cn.intelligent;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class Test {

	 @RequestMapping(value="/conn.do")
	 public @ResponseBody String login(HttpServletRequest request) {
		 
		 return "HELLO WORLD";
	 }
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
