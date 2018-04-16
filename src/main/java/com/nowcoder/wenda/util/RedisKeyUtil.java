package com.nowcoder.wenda.util;

/**
 * 用于统一key的名称格式，保证每次创建的key不会重复出错
 * @author XX
 *
 */
public class RedisKeyUtil {
	private static String split =":";
	public static String setLikeKey(int entityType,int entityId) {
		
		return "like" + split + entityType + split + entityId;
	}
	
	public static String setDislikeKey(int entityType ,int entityId) {
		return "dislike" + split + entityType + split +entityId;
	}
	
}
