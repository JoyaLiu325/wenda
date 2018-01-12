package com.nowcoder.wenda.model;

import java.util.HashMap;
import java.util.Map;

//用来传递数据和模板之间的对象	
public class ViewObject {
	private Map<String,Object> obj = new HashMap<>();
	
	public void set(String key,Object value) {
		obj.put(key, value);
	}
	
	public Object get(String key){
		return obj.get(key);
	}
}
