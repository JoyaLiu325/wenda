package com.nowcoder.wenda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.nowcoder.wenda.dao.CommentDAO;
import com.nowcoder.wenda.model.Comment;

@Service
public class CommentService {
	@Autowired
	CommentDAO commentDAO;
	@Autowired
	SensitiveService sensitiveService;
	
/**
 * 获取实体的所有评论	
 * @param entityId
 * @param entityType
 * @param limit
 * @param offset
 * @return
 */
	public List<Comment> getCommentByEntity(int entityId,int entityType,int limit,int offset){ 
		return commentDAO.getCommentByEntity(entityId, entityType, limit, offset);
	}
	
/**
 * 添加评论
 * @param comment
 * @return
 */
	public int addComment(Comment comment) {
//		过滤
		comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
		String content = sensitiveService.filter(comment.getContent());
		comment.setContent(content);
//		根据返回值判断添加评论是否成功
		return commentDAO.addComment(comment)>0 ? comment.getId() : 0;
	}
	
/**
 * 获取评论数量
 * @param entityId
 * @param entityType
 * @return
 */
	public int getCommentCount(int entityId,int entityType) {
		return commentDAO.getCommentCount(entityId, entityType);
	}
	
/**
 * 删除评论
 * @param id
 * @return
 */
//	删除评论
	public boolean deleteComment(int id) {
		return commentDAO.updateCommentStatus(id, 1) > 0;
	}
	
	public static void main(String[] args) {
		CommentService commentService = new CommentService();
		List<Comment> comments = commentService.getCommentByEntity(17, 1, 0, 0);
		for(Comment comment : comments) {
			System.out.println(comment.getEntityId());
		}
	}
	
}
