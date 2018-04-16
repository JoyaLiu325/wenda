package com.nowcoder.wenda.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.wenda.model.HostHolder;
import com.nowcoder.wenda.model.Message;
import com.nowcoder.wenda.model.ViewObject;
import com.nowcoder.wenda.service.MessageService;
import com.nowcoder.wenda.service.UserService;
import com.nowcoder.wenda.util.WendaUtil;

@Controller
public class MessageController {
	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	MessageService messageService;
	@Autowired
	HostHolder hostholder;
	@Autowired
	UserService userService;
	
	@RequestMapping(path= {"/msg/addMessage"}, method= {RequestMethod.POST})
	@ResponseBody
//	param来自前端的数据，名称应该与前端一致，参看popmsg.js
	public String addMessage(@RequestParam("toName") String toName,@RequestParam("content") String content) {
		try {
			if(hostholder.getUser() ==null) {
				return WendaUtil.getJSONString(999, "未登录");
			}
			if(toName == null) {
				return WendaUtil.getJSONString(998, "接收用户不存在");
			}
			Message message = new Message();
			message.setContent(content);
			message.setToId(userService.selectUserByName(toName).getId());
			message.setFromId(hostholder.getUser().getId());
			message.setCreatedDate(new Date());
			String conversationId = message.getFromId()<message.getToId() 
					? String.format("%d_%d", message.getFromId(),message.getToId()) 
					: String.format("%d_%d", message.getToId(),message.getFromId());
			message.setConversationId(conversationId);
			messageService.addMessage(message);
//			0表示成功
			return WendaUtil.getJSONString(0);
			
		}catch (Exception e) {
			logger.error("发送消息失败" + e.getMessage());
//			疑问 : 前端获取数据 {"msg":"发信失败","code":1}
			return WendaUtil.getJSONString(1, "发信失败" );
		}
	}
	
	@RequestMapping(path = {"/msg/list"} , method = {RequestMethod.GET})
	public String getConversationList(Model model ) {
		if(hostholder.getUser() ==null)
			return "redirect:/reglog";
		int userId = hostholder.getUser().getId();
		List<Message>  messageList = messageService.getMessageList(userId, 0, 10);
		List<ViewObject> voList = new ArrayList<>();
		for(Message message : messageList) {
			ViewObject vo = new ViewObject();
			vo.set("message", message);
			vo.set("user", userId == message.getToId() ? userService.getUser(message.getFromId()) : userService.getUser(message.getToId()));
			vo.set("unRead", messageService.getUnreadCount(message.getConversationId(), userId ));
			voList.add(vo);
		}
		model.addAttribute("messageList", voList);
		return "letter";
	}
	
	@RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
	public String getConversationDetail(Model model ,@Param("conversationId") String conversationId ) {
		messageService.updateStatus(conversationId,hostholder.getUser().getId());
		List<Message> messageList = messageService.getMessageByConversationId(conversationId, 0, 10);
		List<ViewObject> voList = new ArrayList<>();
		for(Message message : messageList) {
			ViewObject vo = new ViewObject();
			vo.set("message", message);
			vo.set("user", userService.getUser(message.getFromId()));
			voList.add(vo);
		}
		model.addAttribute("voList",voList);
		return "letterDetail";
	}
}
