package com.nowcoder.wenda.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nowcoder.wenda.model.Message;

@Mapper
public interface MessageDAO {
	String table_name = " message ";
	String insert_fields = " to_id,from_id,content,conversation_id,created_date,has_read ";
	String select_fields = " id, " + insert_fields;
	
//	返回值是新插入行的主键
//	插入时保持变量名顺序一一对应
	@Insert({"insert into " + table_name + "(" + insert_fields + ") values(#{toId},#{fromId},#{content},#{conversationId},#{createdDate},#{hasRead})"})
	int addMessage(Message message); 
	
//	param中的变量为mybatis.xml中的变量 ,后面的变量为传给mybatis.xml的变量值
	@Select({"select "+select_fields + "from"+  table_name + "where conversation_id=#{conversationId}"
			+ " order by created_date desc limit #{offset},#{limit} " })
	List<Message> getMessageByConversationId(@Param ("conversationId") String conversationId,
										@Param ("offset") int offset,
										@Param ("limit") int limit);
	
//	查找关于某个用户的所有信息
//  1.select * ,count(id) from (select * from message order by created_date desc limit 9999) b group by (conversation_id);
//	2.select from_id,to_id,content,has_read,conversation_id,b.date,b.id from message ,
//	( select max(created_date) as date ,count(id) as id from message where message.from_id =3 or message.to_id=3 group by conversation_id) b 
//	where b.date = message.created_date;
	@Select({"select " + " from_id,to_id,conversation_id,has_read,content ,b.id as id,b.date as created_date "
			+ " from "+table_name+",( select max(created_date) as date ,count(id) as id "
					+ " from message where from_id = ${userId} or to_id = ${userId} "
					+ " group by conversation_id) as b "
					+ " where message.created_date = b.date limit #{offset},#{limit}"})
	List<Message> getMessageList(@Param ("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);
	
	
	@Update({" update "+ table_name + " set has_read = 1 where conversation_id = #{conversationId} and to_id = #{toId}" })
	int updateStatus(@Param("conversationId") String conversationId,@Param("toId") int toId);
	
	@Select({"select count(id) from" + table_name + " where conversation_id = #{conversationId} and to_id = #{toId} and has_read = 0"})
	int getUnreadCount(@Param("conversationId") String conversationId,@Param("toId") int toId);
}
