package com.cn.intelligent.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.intelligent.service.Calculation;

@Controller
@RequestMapping("/cal")
public class CalculationController {

	 @RequestMapping(value="/get.do")
	 public @ResponseBody Set<String> login(HttpServletRequest request) {
		 int len = Integer.parseInt(request.getParameter("len"));
		 int max = Integer.parseInt(request.getParameter("max"));
		 int type = Integer.parseInt(request.getParameter("type"));
		 Calculation c  = new Calculation(len, max, type);
		 return c.getData();
	 }
}
