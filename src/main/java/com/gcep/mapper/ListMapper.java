package com.gcep.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gcep.model.ListModel;

public class ListMapper implements RowMapper<ListModel> {

	@Override
	public ListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
