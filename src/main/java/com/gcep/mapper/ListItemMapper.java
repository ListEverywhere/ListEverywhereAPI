package com.gcep.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gcep.model.ListItemModel;

/**
 * Mapper class for ListItem objects from the database
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public class ListItemMapper implements RowMapper<ListItemModel> {

	@Override
	public ListItemModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
