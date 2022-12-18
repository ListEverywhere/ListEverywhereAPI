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
		return new ListModel(rs.getInt("list_id"),
				rs.getString("list_name"),
				rs.getDate("creation_date").toLocalDate(),
				rs.getDate("last_modified").toLocalDate(),
				rs.getInt("user_id"),
				null);
	}
	
	
}
