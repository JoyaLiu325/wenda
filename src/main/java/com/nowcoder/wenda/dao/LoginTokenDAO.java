package com.nowcoder.wenda.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nowcoder.wenda.model.LoginToken;

@Mapper
public interface LoginTokenDAO {
	String table = " login_token ";
	String insert_fields = " user_id,token,expired,status ";
	String select_fields = " id, "+insert_fields;
	
	@Insert({"insert into",table,"(",insert_fields,") values (#{userId},#{token},#{expired},#{status})"})
	int addToken(LoginToken lt);

	//	在每次登录时，要查询token是否存在
	@Select({"select ",select_fields,"from",table,"where token=#{token}"})
	LoginToken selectByToken(@Param("token") String token);
	
	//登出时，修改status
	@Update({"update ",table,"set status=#{status} where token=#{token}"})
	int updateStatus(@Param("token") String token,@Param("status") int status);
	
	
}
