package com.nowcoder.wenda.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nowcoder.wenda.model.Question;

@Mapper
public interface QuestionDAO {
	String table_name = " question ";
	String insert_fields = " title,content,user_id,created_date,comment_count ";
	String select_fields = " id, " + insert_fields;
	
	@Insert({"insert into " + table_name + "(" + insert_fields + ") values(#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
	int addQuestion(Question question);
	
//	param中的变量为mybatis.xml中的变量 ,后面的变量为传给mybatis.xml的变量值
	List<Question> selectLatestQuestion(@Param ("userId") int userId,
										@Param ("offset") int offset,
										@Param ("limit") int limit);
	
	
	
}
