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
		return new ListItemModel(rs.getInt("item_id"),
				"Unknown",
				rs.getBoolean("checked"),
				rs.getInt("position"),
				rs.getInt("list_item_id"),
				rs.getInt("list_id"));
	}

}
