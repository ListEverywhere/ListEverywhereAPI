package com.gcep.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gcep.model.ListModel;

/**
 * Mapper class for Shopping List objects from the database
 * @author Gabriel Cepleanu
 * @version 0.1
 */
public class ListMapper implements RowMapper<ListModel> {

	@Override
	public ListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
