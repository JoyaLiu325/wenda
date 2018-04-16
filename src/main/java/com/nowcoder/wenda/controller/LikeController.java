package com.nowcoder.wenda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.wenda.model.EntityType;
import com.nowcoder.wenda.model.HostHolder;
import com.nowcoder.wenda.model.User;
import com.nowcoder.wenda.service.LikeService;
import com.nowcoder.wenda.util.WendaUtil;

@Controller
public class LikeController {
	@Autowired
	LikeService likeService;
	@Autowired
	HostHolder hostholder;
	
	@RequestMapping(path = {"/like"},method = {RequestMethod.POST})
	@ResponseBody
	public String like(@RequestParam("commentId") int commentId) {
		User user = hostholder.getUser();
		if(user == null)
			return WendaUtil.getJSONString(999);
		Long likeCount = likeService.like(user.getId(), EntityType.ENTITY_COMMENT, commentId);
		return WendaUtil.getJSONString(0, String.valueOf(likeCount));
	}
	
	@RequestMapping(path = {"/dislike"},method = {RequestMethod.POST})
	@ResponseBody
	public String dislike(@RequestParam("commentId") int commentId) {
		User user = hostholder.getUser();
		if(user == null)
			return WendaUtil.getJSONString(999);
		Long dislikeCount = likeService.dislike(user.getId(), EntityType.ENTITY_COMMENT, commentId);
		return WendaUtil.getJSONString(0, String.valueOf(dislikeCount));
	}
	
}
