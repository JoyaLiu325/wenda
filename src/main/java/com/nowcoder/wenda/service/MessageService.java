package com.nowcoder.wenda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.nowcoder.wenda.dao.MessageDAO;
import com.nowcoder.wenda.model.Message;

@Service
public class MessageService {
	@Autowired
	MessageDAO messageDAO;
	@Autowired
	SensitiveService sensitiveService;
	
	public int addMessage(Message message) {
		message.setContent(HtmlUtils.htmlEscape(message.getContent()));
		message.setContent(sensitiveService.filter(message.getContent()));
		return messageDAO.addMessage(message);
	}
	
	public List<Message> getMessageByConversationId(String conversationId,int offset,int limit){
		return messageDAO.getMessageByConversationId(conversationId, offset, limit);
	}
	
	public List<Message> getMessageList(int userId,int offset,int limit){
		return messageDAO.getMessageList(userId, offset, limit);
	}
	
	public int updateStatus(String conversationId,int toId) {
		return messageDAO.updateStatus(conversationId, toId);
	}
	
	public int getUnreadCount(String conversationId,int toId) {
		return messageDAO.getUnreadCount(conversationId, toId);
	}
}
