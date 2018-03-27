package com.nowcoder.wenda.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nowcoder.wenda.model.Comment;
import com.nowcoder.wenda.model.EntityType;
import com.nowcoder.wenda.model.HostHolder;
import com.nowcoder.wenda.service.CommentService;
import com.nowcoder.wenda.service.QuestionService;
import com.nowcoder.wenda.util.WendaUtil;

@Controller
public class CommentController {
	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	HostHolder   hostHolder;
	@Autowired
	CommentService commentService; 
	@Autowired
	QuestionService questionService; 
	
	@RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
	public String addComment(@RequestParam("questionId") int questionId,
				@RequestParam("content") String content) {
		try {
		Comment comment = new Comment();
		if(hostHolder.getUser()!= null)
			comment.setUserId(hostHolder.getUser().getId());
		else
			comment.setUserId(WendaUtil.ANONYMOUS_USERID);
		comment.setCreatedDate(new Date());
		comment.setEntityId(questionId);
		comment.setEntityType(EntityType.ENTITY_QUESTION);
		comment.setContent(content);
		comment.setStatus(0);
		commentService.addComment(comment);
//		评论数在每次评论的时候进行修改，因此即使初始状态有评论，
//		但是没有没有发评论这个行为，评论数默认值仍为0
//		用上数据库事务：对于两个操作必须同时成功或者同时失败
		int count = commentService.getCommentCount(questionId,EntityType.ENTITY_QUESTION);
		questionService.updateCommentCount(count, questionId);
		}
		catch (Exception e) {
			logger.error("增加评论失败" + e.getMessage());
		}
		return "redirect:/question/" + questionId;
	}
}
