package com.nowcoder.wenda.controller;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nowcoder.wenda.model.User;

@Controller
//spring 通过注解的方式表示各个类的作用
//访问首页
public class IndexController {

//	访问以下函数的地址,多地址对应相同的网页
	@RequestMapping(path= {"/","/index"})
//	表示直接返回一个字符串而不是模板
	@ResponseBody
	public String index() {
		return "indexpage";
	}
	
//	访问以下函数的地址,多地址对应相同的网页,userId用中括号表示变量
	@RequestMapping(path= {"/profile/{groupId}/{userId}"})
//	表示直接返回一个字符串而不是模板
	@ResponseBody
	
	public String profile(@PathVariable("groupId") String groupId,
						  @PathVariable("userId") int userId,
						  @RequestParam(value="type",defaultValue = "1") int type,
						  @RequestParam(value="key",required = false) String key) {
		return String.format("profile page of %d,%s,type = %d,key = %s",userId,groupId,type,key);
	}
	
//	访问以下函数的地址,多地址对应相同的网页
	@RequestMapping(path= {"/html"})
//	表示直接返回一个字符串而不是模板
	public String html(Model model) {
		model.addAttribute("value", 123);
//		创建一个固定大小的数组
		List<String> colors = Arrays.asList(new String[] {"RED","GREEN","BLUE"});
		model.addAttribute("colors",colors);
		Map<String,String> map  = new HashMap<>();
		for(int i = 1;i<4;i++) {
			map.put(String.valueOf(i), String.valueOf(i*i));
		}
		model.addAttribute("map",map);
//		model.addAttribute("User",new User("Tom"));
		return "home";
	}
	
	@RequestMapping(path= {"/request"})
	@ResponseBody
	public String request(Model model,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		Enumeration<String> headerNames = request.getHeaderNames();
		StringBuilder builder = new StringBuilder();
		while(headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			builder.append(name + ":" + request.getHeader(name) + "<br>");
		}
		builder.append(request.getCookies());
		
/*		
		builder.append(request.getMethod()+"<br>");
		builder.append(request.getQueryString()+"<br>");
		builder.append(request.getPathInfo()+"<br>");
		builder.append(request.getRequestURI()+"<br>");*/
		return builder.toString();
	}
	
	@RequestMapping(path= {"/admin"})
	@ResponseBody
	public String admin(@RequestParam("value") String value) {
		if(value.equals("admin"))
			return "hello admin";
		throw new IllegalArgumentException("error");
		
	}
	
	//异常捕获，对异常的统一处理函数
	@ExceptionHandler()
	@ResponseBody
	public String error(Exception e) {
		return "error" + e.getMessage();
	}
	
	
	
}
