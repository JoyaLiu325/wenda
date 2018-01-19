package com.nowcoder.wenda;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nowcoder.wenda.dao.LoginTokenDAO;
import com.nowcoder.wenda.dao.UserDAO;
import com.nowcoder.wenda.model.LoginToken;
import com.nowcoder.wenda.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SelectDatabaseTests {
	@Autowired
	UserDAO userDAO;
	@Autowired
	LoginTokenDAO loginTokenDAO;
	
	@Test
	public void Test() {
		String token = "3e9155dc1d2349e5b8d950d4e2554468";
		LoginToken loginToken =loginTokenDAO.selectByToken(token);
		System.out.println(loginToken.getUserId());
	}
	
}
