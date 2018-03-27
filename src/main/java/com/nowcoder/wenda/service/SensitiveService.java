package com.nowcoder.wenda.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.management.BufferPoolMXBean;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


@Service
public class SensitiveService implements InitializingBean{
	 private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
	 
	private class TrieNode { 
		private boolean end = false;
		//存放子节点值和对应的子节点
		private Map<Character, TrieNode> subNodes = new HashMap<>();
//		添加子结点
		public void addSubNode(Character key,TrieNode node) {
			subNodes.put(key, node);
		}
//		获取结点信息
		public TrieNode getSubNode(Character key) {
			return subNodes.get(key);
		}
//		判断是否是字符串结尾	
		public boolean isKeywordEnd() 
		{
			return this.end;
		}
//		给尾结点赋值
		public void setKeywordEnd(boolean end) {
			this.end = end;
		}
	}
	
//	创建根结点
	private TrieNode rootNode = new TrieNode();
	
//	添加敏感词的时候也要进行字符过滤
	private  void addWord(String lineText) {
		lineText = lineText.trim();
		TrieNode tempNode = rootNode;
		for(int i =0;i<lineText.length();i++) {
			Character ch = lineText.charAt(i);
			if(isSymbol(ch)) {
				continue;
			}
				if(tempNode.getSubNode(ch)!=null) {
					tempNode = tempNode.getSubNode(ch);
				}else {
//					subNode为指针，该操作通常将subNode指向一个对象，而非创建一个对象
//					TrieNode subNode = null;
					TrieNode subNode = new TrieNode(); 
					tempNode.addSubNode(ch, subNode );
					tempNode = tempNode.getSubNode(ch);
				}
				if(i == lineText.length()-1)
					tempNode.setKeywordEnd(true);
			}
		}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			Resource resource = new ClassPathResource("SensitiveWords.txt");
			File file = resource.getFile();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			if(!file.exists())
				System.out.println("error");
			String tempLine = null;
			while((tempLine = reader.readLine())!=null) {
				tempLine = tempLine.trim();
//				System.out.println(tempLine);
				addWord(tempLine);
			}
			reader.close();
		} catch (Exception e) {
			logger.error("读取敏感词失败",e.getMessage());
		}
	}
	
//	设置过滤特殊字符函数
	private boolean isSymbol(char c) {
		int cint= (int) c;	
//		东亚字符
		return !CharUtils.isAsciiAlphanumeric(c) && (cint<0x2e80 || cint>0x9fff);
	}

//设置两个指针表示敏感词的首尾
	public String filter(String text) {
		if(StringUtils.isBlank(text))
			return text;
		int begin = 0;
		int index = 0;
		TrieNode tempNode =rootNode;
		StringBuffer sb = new StringBuffer();
		while(index<text.length()) {
			char  ch = text.charAt(index);
			if(isSymbol(ch)) {
				if(tempNode == rootNode) {
					sb.append(ch);
					begin++;
				}
				index++;
				continue;
			}
			tempNode = tempNode.getSubNode(ch);
			if(tempNode == null) {
				sb.append(text.charAt(begin));
//				sb.append(ch);
				index = ++begin;
				tempNode = rootNode;
			}else if(tempNode.isKeywordEnd()) {
				sb.append("***");
				begin = ++index;
				tempNode = rootNode;
			}else {
				index++;
			}
		}
		if(begin<text.length())
			sb.append(text.substring(begin));
		return sb.toString();

	}
	
/*	public static void main(String[] args) {
//		实例化当前类，调用类中的方法
		try {
//			类路径资源的资源实现。使用给定的ClassLoader或给定的类来加载资源。
			Resource resource = new ClassPathResource("SensitiveWordS.txt");
			File file = resource.getFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String tempLine = null;
			while((tempLine = br.readLine())!=null) {
				tempLine = tempLine.trim();
				System.out.println(tempLine);
			}
			br.close();
		} catch (Exception e) {
			logger.error("读取敏感词失败",e.getMessage());
		}
	}*/
}
