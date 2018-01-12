package com.nowcoder.wenda.model;

import org.springframework.stereotype.Component;

//实现多用户同时访问user变量
@Component
public class HostHolder {
//	每一个线程都有自己的变量，副本变量，线程本地变量
	private static ThreadLocal<User> users = new ThreadLocal<User>();
		
//	获取当前线程对应的user变量
	public User getUser() {
		return users.get();
	}
	
	public void setUser(User user) {
		users.set(user); 
	}
	
	public void clear() {
		users.remove();	
	}
}
