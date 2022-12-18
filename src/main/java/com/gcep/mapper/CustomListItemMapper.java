package com.gcep.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gcep.model.CustomListItemModel;

/**
 * Mapper class for CustomListItem objects from the database
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public class CustomListItemMapper implements RowMapper<CustomListItemModel> {

	@Override
	public CustomListItemModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new CustomListItemModel(-1,
				rs.getString("item_name"),
				rs.getBoolean("checked"),
				rs.getInt("custom_item_id"),
				-1);
	}

}
