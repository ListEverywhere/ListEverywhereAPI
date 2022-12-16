package com.gcep.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gcep.model.UserModel;

public class UserMapper implements RowMapper<UserModel> {

	@Override
	public UserModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new UserModel(rs.getInt("user_id"),
				rs.getString("first_name"),
				rs.getString("last_name"),
				rs.getString("email"),
				rs.getDate("date_of_birth").toLocalDate(),
				rs.getString("username"),
				rs.getString("password"));
	}


}
