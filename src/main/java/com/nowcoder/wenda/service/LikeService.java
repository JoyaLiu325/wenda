package com.nowcoder.wenda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.wenda.util.JedisAdapter;
import com.nowcoder.wenda.util.RedisKeyUtil;

@Service
public class LikeService {
	@Autowired
	JedisAdapter jedisadapter;
//	点赞
	public long like(int userId,int entityType ,int entityId) {
		String likeKey = RedisKeyUtil.setLikeKey(entityType, entityId);
		jedisadapter.sadd(likeKey, String.format("%d", userId));
//		点赞的时候删除踩的动作
		String dislikeKey = RedisKeyUtil.setDislikeKey(entityType, entityId);
		jedisadapter.srem(dislikeKey, String.format("%d", userId));
		return jedisadapter.scard(likeKey);
	}
//	踩
	public long dislike(int userId,int entityType ,int entityId) {
		String likeKey = RedisKeyUtil.setLikeKey(entityType, entityId);
		jedisadapter.srem(likeKey, String.format("%d", userId));
		String dislikeKey = RedisKeyUtil.setDislikeKey(entityType, entityId);
		jedisadapter.sadd(dislikeKey, String.format("%d", userId));
//		点踩后还是应该返回likeKey的值，而不是dislikeKey的值
		return jedisadapter.scard(likeKey);
	}
	
//	当页面打开时点亮喜欢或者不喜欢图标
	public int getlikeStatus(int userId,int entityType,int entityId) {
		String likeKey = RedisKeyUtil.setLikeKey(entityType, entityId); 
		String dislikeKey = RedisKeyUtil.setDislikeKey(entityType, entityId);
		if(jedisadapter.sismember(likeKey, String.format("%d", userId))	)
			return 1;
		else if(jedisadapter.sismember(dislikeKey, String.format("%d", userId)))
			return -1;
		else
			return 0;
	}
	
//	页面获取当前喜欢的人数
	public long getLikeCount(int entityType,int entityId) {
		String likeKey = RedisKeyUtil.setLikeKey(entityType, entityId); 
		return jedisadapter.scard(likeKey);
	}
	
}
