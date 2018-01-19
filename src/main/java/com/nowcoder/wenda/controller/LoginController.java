package com.nowcoder.wenda.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nowcoder.wenda.service.UserService;

@Controller
public class LoginController {
//	打印出日志信息所在类
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	UserService userService;
	
//	登录注册页面
	@RequestMapping(path= {"/reglogin"}, method= {RequestMethod.GET})
	public String reg(Model model,@RequestParam(value = "next" ,required = false) String next) {
		model.addAttribute("next",next);
		return "login";
	}
	
//	处理注册逻辑，注册成功则回到首页
	@RequestMapping(path= {"/reg"},method= {RequestMethod.POST})
	public String reg(Model model,
					  @RequestParam("username") String name,
					  @RequestParam("password") String password,
					  @RequestParam(value="rememberme",defaultValue="false") boolean rememberme,
					  @RequestParam(value = "next" ,required = false) String next,
					  HttpServletResponse response) {
		try {
			Map<String,String> map = new HashMap<>();
			map = userService.register(name, password);
			if(map.containsKey("token")) {
				Cookie cookie = new Cookie("token", map.get("token"));
				cookie.setPath("/");
				if(rememberme == true)
//					设置为5天
					cookie.setMaxAge(5*24*60*60);
				response.addCookie(cookie);
				if(StringUtils.isNotBlank(next))
					return "redirect:"+next;
//				不带参数的跳转到另一个controller中
				return "redirect:/";
			}
			else {
				model.addAttribute("msg",map.get("msg"));
				return "login";
			}
		}catch (Exception e) {
			logger.error("注册异常",e.getMessage());
			return "login";
		}
		
	}
	
	//处理登录逻辑
	@RequestMapping(path= {"/login"},method= {RequestMethod.POST})
	public String login(Model model,
						@RequestParam("username") String username,
						@RequestParam("password") String password,
						@RequestParam(value = "next" ,required = false) String next,
						@RequestParam(value = "rememberme" ,defaultValue = "false") boolean rememberme,
						HttpServletResponse response) {
		try {
			Map<String, String> map = new HashMap<>();
			map = userService.login(username, password);
			if(map.containsKey("token")) {
				Cookie cookie = new Cookie("token", map.get("token"));
				cookie.setPath("/");
				if(rememberme == true)
//					设置为5天
					cookie.setMaxAge(5*24*60*60);
				response.addCookie(cookie);
				if(StringUtils.isNotBlank(next))
					return "redirect:"+next;
				else
					return "redirect:/";
			}
			else {
				model.addAttribute("msg",map.get("msg"));
				return "login";
			}
		} catch (Exception e)	 {
			e.printStackTrace();
			logger.error("登录异常", e.getMessage());
			return "login";
		}
	}
	
	//用户登出
	@RequestMapping(path= {"/logout"},method= {RequestMethod.GET})
	public String logout(@CookieValue("token") String token) {
		userService.logout(token);
		return "redirect:/";
	}
	

}
