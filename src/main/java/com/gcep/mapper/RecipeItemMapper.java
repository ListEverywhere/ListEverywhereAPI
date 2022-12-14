package com.gcep.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gcep.model.RecipeItemModel;

public class RecipeItemMapper implements RowMapper<RecipeItemModel> {

	@Override
	public RecipeItemModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		return new RecipeItemModel(
				rs.getInt("item_id"),
				"Unknown",
				false,
				rs.getInt("recipe_item_id"),
				rs.getInt("recipe_id")
				);
	}

}
