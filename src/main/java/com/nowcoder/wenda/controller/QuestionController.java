package com.nowcoder.wenda.controller;

import java.util.Date;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.wenda.model.HostHolder;
import com.nowcoder.wenda.model.Question;
import com.nowcoder.wenda.model.User;
import com.nowcoder.wenda.service.QuestionService;
import com.nowcoder.wenda.service.UserService;
import com.nowcoder.wenda.util.WendaUtil;

@Controller
public class QuestionController {
	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	HostHolder hostHolder;
	@Autowired
	QuestionService questionService;
	@Autowired
	UserService userService;
	
	@RequestMapping(path="/question/add",method= {RequestMethod.POST})
	@ResponseBody
	public String addQuestion(@RequestParam("title") String title,@RequestParam("content") String content) {
		try {	
			Question question = new Question();
			question.setTitle(title);
			question.setContent(content);
			question.setCreatedDate(new Date());
			question.setCommentCount(0);
			if(hostHolder.getUser() == null) {
//				question.setUserId(WendaUtil.ANONYMOUS_USERID);
//				当前无用户时，提交问题后，会返回code999， 在popadd.js文件中将会跳转到reglogin页面
				return WendaUtil.getJSONString(999);
			}else
				question.setUserId(hostHolder.getUser().getId());
			if(questionService.addQuestion(question)>0) {
				return WendaUtil.getJSONString(0);
			}
		} catch (Exception e) {
//			创建问题失败的原因是执行过程中的问题
			logger.error("增加题目失败 ", e.getMessage());
		}
//		创建问题失败的原因是插入语句有问题
			return WendaUtil.getJSONString(1,"失败");
	}
	
	@RequestMapping(path="/question/{qid}",method= {RequestMethod.GET})
	public String questionDetail(Model model,@PathVariable("qid") int qid) {
		Question question = questionService.selectById(qid);
		User user = userService.getUser(question.getUserId());
		model.addAttribute("question",question);
		model.addAttribute("user",user);
		return "detail";
	}
	
	
}
