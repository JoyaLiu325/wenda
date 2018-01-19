package com.nowcoder.wenda.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.nowcoder.wenda.model.Question;

@Mapper
public interface QuestionDAO {
	String table_name = " question ";
	String insert_fields = " title,content,user_id,created_date,comment_count ";
	String select_fields = " id, " + insert_fields;
	
//	返回值是新插入行的主键
	@Insert({"insert into " + table_name + "(" + insert_fields + ") values(#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
	int addQuestion(Question question); 
	
//	param中的变量为mybatis.xml中的变量 ,后面的变量为传给mybatis.xml的变量值
	List<Question> selectLatestQuestion(@Param ("userId") int userId,
										@Param ("offset") int offset,
										@Param ("limit") int limit);
	
	@Select({"select "+select_fields + "from"+  table_name + "where id=#{id}" })
	Question selectById(int id);
}
