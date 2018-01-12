package com.nowcoder.wenda.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.nowcoder.wenda.interceptor.LoginRequiredInterceptor;
import com.nowcoder.wenda.interceptor.PassportInterceptor;

//
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter{
	@Autowired
	PassportInterceptor passportInterceptor;
	@Autowired
	LoginRequiredInterceptor loginRequiredInterceptor;
	
	@Override
//	配置一系列的映射拦截器
	public void addInterceptors(InterceptorRegistry registry) {
		/**************************
		 * 存疑
		 * 
		 * 
		 * 存疑
		 **************************/
//		将拦截器添加到拦截器链中，链应该在父类中？？
		registry.addInterceptor(passportInterceptor);
//		访问/user/*页面时需要经过该拦截器，否则不经过
		registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
		super.addInterceptors(registry);
	}
	

}
