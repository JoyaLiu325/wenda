package com.nowcoder.wenda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.wenda.dao.QuestionDAO;
import com.nowcoder.wenda.model.Question;

@Service
public class QuestionService {
	@Autowired
	QuestionDAO questionDAO;
	
	public List<Question> selectLatestQuestion(int userId,int offset,int limit) {
		return questionDAO.selectLatestQuestion(userId, offset, limit);
		
	}
}
