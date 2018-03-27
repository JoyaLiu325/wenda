package com.nowcoder.wenda.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.descriptor.web.LoginConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.wenda.dao.LoginTokenDAO;
import com.nowcoder.wenda.dao.UserDAO;
import com.nowcoder.wenda.model.LoginToken;
import com.nowcoder.wenda.model.User;
import com.nowcoder.wenda.util.WendaUtil;

@Service
public class UserService {
	@Autowired
	UserDAO userDAO;
	@Autowired
	LoginTokenDAO loginTokenDAO;
	/**
	 * 注册
	 * @param name
	 * @param password
	 * @return
	 */
	public Map<String,String> register(String name,String password){
		Map<String,String> map = new HashMap<>();
		if(StringUtils.isBlank(name)) {
			map.put("msg", "用户名为空");
			return map;
		}
//		是否可以用else if
		if(StringUtils.isBlank(password)) {
			map.put("msg", "密码为空");
			return map;
		}
		if(userDAO.selectByName(name)!=null) {
			map.put("msg", "用户名已存在");
			return map;
		}
			User user = new User();
			user.setName(name);
			user.setSalt(UUID.randomUUID().toString().substring(0, 5));
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
			user.setPassword(WendaUtil.MD5(user.getSalt()+password));
			userDAO.addUser(user);
//			此时的user的id 有值吗？？
			String token = this.addToken(user.getId());
			map.put("token", token);
			return map;
		
	}
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	public Map<String,String> login(String username,String password){
		Map<String,String> map = new HashMap<>();
		if(username == null) {
			map.put("msg", "用户名为空");
			return map;
		}
		if(password == null) {
			map.put("msg", "密码为空");
			return map;
		}
		
		User user = userDAO.selectByName(username);
		if( user == null) {
			map.put("msg", "该用户不存在");
			return map;
		}
		
		if(!WendaUtil.MD5(user.getSalt()+password).equals(user.getPassword())) {
			map.put("msg", "密码错误");
			return map;
		}
		
		String token = this.addToken(user.getId());
		map.put("token", token);
		return map;
		
	}

	/**
	 * 用户登出
	 * @param token
	 */
	public void logout(String token) {
		loginTokenDAO.updateStatus(token, 1);
	}
	
	/**
	 * 添加token，返回token值
	 * @param userId
	 * @return
	 */
	public String addToken(int userId) {
		LoginToken loginToken = new LoginToken();
		loginToken.setUserId(userId);
		loginToken.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
		Date now = new Date();
//		设置过期时间为一天
		now.setTime(1000*3600*24+now.getTime());
		loginToken.setExpired(now);
		loginToken.setStatus(0);
		loginTokenDAO.addToken(loginToken); 
		return loginToken.getToken();
	}
	
	/**
	 * 通过用户id获取用户信息
	 * @param id
	 * @return
	 */
	public User getUser(int id) {
		return userDAO.selectById(id);
	}
	
	/**
	 * 通过用户名获取用户
	 * @param name
	 * @return
	 */
	public User selectUserByName(String name) {
		return userDAO.selectByName(name);
	}

}
