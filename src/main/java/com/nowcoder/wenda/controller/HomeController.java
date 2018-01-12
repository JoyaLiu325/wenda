package com.nowcoder.wenda.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nowcoder.wenda.model.Question;
import com.nowcoder.wenda.model.ViewObject;
import com.nowcoder.wenda.service.QuestionService;
import com.nowcoder.wenda.service.UserService;

@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	UserService userService;
	@Autowired
	QuestionService questionService;
	
	@RequestMapping(path= {"/user/{userId}"})
	public String userIndex(Model model, @PathVariable("userId") int userId) {
		List<ViewObject> voList = getQuestions(userId, 0, 1);
		model.addAttribute("voList",voList);
		return "index";
	}
	@RequestMapping(path= {"/","index"},method = {RequestMethod.GET})
	public String index(Model model) {
		//读10个数据
		List<ViewObject> voList = getQuestions(0, 0, 10);
		model.addAttribute("voList",voList);
		return "index";
	}
	
	public List<ViewObject> getQuestions(int userId,int offset,int limit) {
		List<Question> questionList = questionService.selectLatestQuestion(userId,offset, limit);
		List<ViewObject> voList = new ArrayList<>();
		for(Question question :questionList) {
//			设置一个模型，存放一个模块中的所有数据：问题和用户信息
			ViewObject vo = new ViewObject();
			vo.set("question",question);
			vo.set("user", userService.getUser(question.getUserId()));
			voList.add(vo);
		}
		return voList;
	}
	

	
}
