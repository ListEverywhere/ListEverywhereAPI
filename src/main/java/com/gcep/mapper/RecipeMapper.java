package com.gcep.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gcep.model.RecipeModel;

public class RecipeMapper implements RowMapper<RecipeModel> {

	@Override
	public RecipeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		return new RecipeModel(
				rs.getInt("recipe_id"),
				rs.getInt("category"),
				rs.getString("recipe_name"),
				rs.getString("recipe_description"),
				null,
				null,
				false
				);
	}

}
