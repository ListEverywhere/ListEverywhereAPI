package com.gcep.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gcep.model.RecipeModel;

/**
 * Mapper class for recipe objects from the database.
 * Does not handle lists for recipe items and steps
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public class RecipeMapper implements RowMapper<RecipeModel> {

	@Override
	public RecipeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		boolean published = rs.getInt("published") == 1;
		
		return new RecipeModel(
				rs.getInt("recipe_id"),
				rs.getInt("category"),
				rs.getString("recipe_name"),
				rs.getString("recipe_description"),
				rs.getInt("cook_time"),
				null,
				null,
				published,
				rs.getInt("user_id")
				);
	}

}
