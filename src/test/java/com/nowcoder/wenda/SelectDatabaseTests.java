package com.nowcoder.wenda;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nowcoder.wenda.dao.UserDAO;
import com.nowcoder.wenda.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SelectDatabaseTests {
	@Autowired
	UserDAO userDAO;
	
	@Test
	public void Test() {
		User user = userDAO.selectById(2);
		System.out.println(user.getName());
	}
	
}
