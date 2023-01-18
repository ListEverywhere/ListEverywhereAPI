package com.gcep.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gcep.model.CategoryModel;

/**
 * Mapper class for category objects from the database.
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public class CategoryMapper implements RowMapper<CategoryModel> {

	@Override
	public CategoryModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		return new CategoryModel(
				rs.getInt("category_id"),
				rs.getString("category_name")
				);
	}

}
