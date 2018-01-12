package com.nowcoder.wenda.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nowcoder.wenda.model.HostHolder;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor{
	@Autowired
	HostHolder hostHolder;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(hostHolder.getUser() == null) {
//			向客户端返回该响应，即向客户端发起请求，让客户端发起请求访问另一个页面，客户端接收到这个请求。
//			sendredirect（）有两个请求和两个响应。
//			返回到登录页面同时把当前页面的url放入跳转的url中。
			response.sendRedirect("/reglogin?next="+request.getRequestURI());
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
	
}
