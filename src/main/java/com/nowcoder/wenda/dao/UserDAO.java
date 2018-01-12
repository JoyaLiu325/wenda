package com.nowcoder.wenda.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.nowcoder.wenda.model.User;

@Mapper
public interface UserDAO {
	String table_name = " user ";
	String insert_fields = " name,password,salt,head_url ";
	String select_fields = " id, " + insert_fields;
//	变量名为user中的变量名
	@Insert({"insert into " , table_name ,"(",insert_fields,") values(#{name},#{password},#{salt},#{headUrl})" })
		int addUser(User user);
	
	User selectByName(@Param ("name") String name);
	
	@Select({"select"+select_fields + "from"+ table_name + "where id = #{id}"})
		User selectById(int id);
	
}
