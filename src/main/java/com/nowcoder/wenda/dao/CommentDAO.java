package com.nowcoder.wenda.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nowcoder.wenda.model.Comment;

@Mapper
public interface CommentDAO {
	String table_name = " comment ";
	String insert_fields = " user_id,content,entity_id,entity_type,created_date,status ";
	String select_fields = " id, " + insert_fields;
/*
 * 定义select的数据get，insert的数据add
 */	
//	返回值是新插入行的主键,该表中为id
//	插入评论
	@Insert({"insert into " + table_name + "(" + insert_fields + ") values(#{userId},#{content},"
			+ "#{entityId},#{entityType},#{createdDate},#{status})"})
	int addComment(Comment comment); 
	
//	param中的变量为mybatis.xml中的变量 ,后面的变量为传给mybatis.xml的变量值
//	选出某个实体的所有评论
	List<Comment> getCommentByEntity(@Param ("entityId") int entityId,
										@Param ("entityType") int entityType,
										@Param ("limit") int limit,
										@Param ("offset") int offset);
	
//	选出某个实体的评论数,需要和question中的变量commentCount进行同步
	@Select({"select count(id) from "+  table_name + 
		"where entity_id=#{entityId} and entity_type = #{entityType}" })
	int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);
	
//删除评论：业务逻辑上是删除某评论，数据库实现上是将该评论的状态进行修改,
//	为什么把status 设置为变量而不是常量1 呢？？
//	update 返回值为更新的行数
	@Update({"update comment set status = #{status} where id = #{id}"})
	int updateCommentStatus(@Param("id") int id,@Param("status") int status);
	
}


