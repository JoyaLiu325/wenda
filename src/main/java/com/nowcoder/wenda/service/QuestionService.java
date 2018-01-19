package com.nowcoder.wenda.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.nowcoder.wenda.dao.QuestionDAO;
import com.nowcoder.wenda.model.Question;

@Service
public class QuestionService {
	@Autowired
	QuestionDAO questionDAO;
	@Autowired
	SensitiveService sensitiveService;
	public int addQuestion(Question question) {
//***** 重要！ 将html字符进行转义，防止xss攻击
		String title = question.getTitle();
		title = HtmlUtils.htmlEscape(title);
		question.setTitle(sensitiveService.filter(title));
//		敏感词过滤
		String content = question.getContent();
		content = HtmlUtils.htmlEscape(content);
		question.setContent(sensitiveService.filter(content));
//		add返回值是新插入行的主键
		
		return questionDAO.addQuestion(question)>0 ? question.getId() : 0;
	}
	
	public List<Question> selectLatestQuestion(int userId,int offset,int limit) {
		return questionDAO.selectLatestQuestion(userId, offset, limit);
		
	}
	
	public Question selectById(int id ) {
		return questionDAO.selectById(id);
	}
}
