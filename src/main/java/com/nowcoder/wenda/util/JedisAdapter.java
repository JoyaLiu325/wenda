package com.nowcoder.wenda.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.nowcoder.wenda.controller.QuestionController;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class JedisAdapter implements InitializingBean{
	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
	private JedisPool pool;

	@Override
	public void afterPropertiesSet() throws Exception {
//		默认使用数据库0
		pool = new JedisPool("redis://localhost:6379/");
	}
	
	public long sadd(String key,String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
//			return values :1 if the new element was added 0 if the element was already a member of the set
			return jedis.sadd(key, value);
		} catch (Exception e) {
			logger.error("redis sadd error" + e.getMessage());
		}finally {
			if(jedis != null)
			jedis.close();
		}
		return 0;
	}
	
	public long srem(String key,String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
//			1 if the new element was removed 0 if the new element was not a member of the set
			return jedis.srem(key, value);
		} catch (Exception e) {
			logger.error("redis srem error" +e.getMessage());
		}finally {
			if(jedis != null)
			jedis.close();
		}
		return 0;
	}
	
	public long scard(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.scard(key);
		}catch(Exception e){
			logger.error("redis scard error" + e.getMessage());
		}finally {
			if(jedis != null)
				jedis.close();
		}
		return 0;
	}
	
	public Boolean sismember(String key,String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.sismember(key,value);
		}catch(Exception e) {
			logger.error("redis sismember error",e.getMessage());
		}finally {
			if(jedis != null)
				jedis.close();
		}
		return false;
	}
	
	
}
