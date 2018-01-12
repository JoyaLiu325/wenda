package com.nowcoder.wenda;

import java.util.Date;
import java.util.Random;

import javax.management.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nowcoder.wenda.dao.QuestionDAO;
import com.nowcoder.wenda.dao.UserDAO;
import com.nowcoder.wenda.model.Question;
import com.nowcoder.wenda.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDatebaseTests {
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	QuestionDAO questionDAO;
	
	@Test
	public void InitDatebase() {
		Random random = new Random();
		
		for(int i = 1;i<11;i++) {
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("user%d", i));
			user.setPassword("");
			user.setSalt("");
//			把user中的所有数据传入到对应的数据库操作语句中的变量
			userDAO.addUser(user);
			
			Question question = new Question();
			question.setTitle(String.format("title%d", i+1));
			question.setContent(String.format("aaaaaaaaasdffffffffffffdfd:%d",i));
			question.setUserId(i+1);
			Date date = new Date();
//			假设创建的时间每次递增1小时，需要转化为毫秒
			date.setTime(date.getTime()+i*3600*1000);
			question.setCreatedDate(date);
			question.setCommentCount(i);
			questionDAO.addQuestion(question);
		}
//		打印list中的三个user对象
	System.out.println(questionDAO.selectLatestQuestion(0,0,3));
	}
}
