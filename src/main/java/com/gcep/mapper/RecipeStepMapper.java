package com.gcep.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gcep.model.RecipeStepModel;

/**
 * Mapper class for recipe step objects from the database.
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public class RecipeStepMapper implements RowMapper<RecipeStepModel> {

	@Override
	public RecipeStepModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		return new RecipeStepModel(
				rs.getInt("recipe_step_id"),
				rs.getString("step_description"),
				rs.getInt("recipe_id")
				);
	}

}
