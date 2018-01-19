package com.nowcoder.wenda.interceptor;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nowcoder.wenda.dao.LoginTokenDAO;
import com.nowcoder.wenda.dao.UserDAO;
import com.nowcoder.wenda.model.HostHolder;
import com.nowcoder.wenda.model.LoginToken;
import com.nowcoder.wenda.model.User;

//component实现依赖注入，把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>
//当组件不好归类的时候，我们可以使用这个注解进行标注。    
@Component
public class PassportInterceptor implements HandlerInterceptor{
	@Autowired
	LoginTokenDAO loginTokenDAO; 
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	HostHolder hostHolder;
	
//	结束后将用户清除
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception modelAndView)
			throws Exception {
		hostHolder.clear();
	}

//	在
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView)
			throws Exception {
//		把该对象应用到所有模板中
		if(modelAndView !=null && hostHolder.getUser() !=null) {
			modelAndView.addObject("user",hostHolder.getUser());
		}
	}

//	在拦截器开始之前就获取当前用户信息，保证之后所有的操作都能访问到该用户
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String token = null;
		if(request.getCookies()!=null) {
			for(Cookie cookie : request.getCookies()) {
				if(cookie.getName().equals("token")) {
					token = cookie.getValue(); 
					break;
				}
			}
		}
		if(token !=null) {
			LoginToken loginToken = loginTokenDAO.selectByToken(token);
			if(loginToken == null || loginToken.getExpired().before(new Date()) || loginToken.getStatus() != 0) {
				return true;
			}
			
			User user = userDAO.selectById(loginToken.getUserId());
			hostHolder.setUser(user);
		}
		
		return true;
	}
	
}
