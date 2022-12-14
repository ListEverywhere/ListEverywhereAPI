package com.gcep.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.gcep.exception.DatabaseErrorException;
import com.gcep.mapper.RecipeMapper;
import com.gcep.model.CategoryModel;
import com.gcep.model.RecipeModel;
import com.gcep.model.RecipeStepModel;

@Repository
public class RecipesDataService implements RecipesDataServiceInterface {
	
	@Autowired
	DataSource dataSource;
	JdbcTemplate jdbc;
	
	public RecipesDataService(DataSource ds) {
		this.dataSource = ds;
		this.jdbc = new JdbcTemplate(ds);
	}

	@Override
	public RecipeModel getRecipeById(int id) {
		RecipeModel recipe = null;
		
		try {
			recipe = jdbc.queryForObject("SELECT recipes.*, users_recipes.* FROM recipes "
					+ "INNER JOIN users_recipes ON users_recipes.recipe_id=recipes.recipe_id WHERE recipes.recipe_id=?", new RecipeMapper(), new Object[] {id});
		}
		catch (EmptyResultDataAccessException e) {
			// did not find a recipe, return null
		}
		catch (Exception e) {
			throw new DatabaseErrorException();
		}
		return recipe;
	}

	@Override
	public List<RecipeModel> getRecipesByUser(int user_id) {
		List<RecipeModel> recipes = null;
		
		try {
			recipes = jdbc.query("SELECT recipes.*, users_recipes.user_id, users_recipes.recipe_id FROM recipes "
					+ "INNER JOIN users_recipes ON recipes.recipe_id=users_recipes.recipe_id WHERE users_recipes.user_id=?", new RecipeMapper(), new Object[] {user_id});
		} catch (Exception e) {
			throw new DatabaseErrorException();
		}
		return recipes;
	}

	@Override
	public List<RecipeModel> getRecipesByCategory(int category) {
		List<RecipeModel> recipes = null;
		
		try {
			recipes = jdbc.query("SELECT recipes.*, categories.category_id, users_recipes.* FROM recipes "
					+ "INNER JOIN categories ON recipes.category=categories.category_id "
					+ "INNER JOIN users_recipes ON users_recipes.recipe_id=recipes.recipe_id WHERE category_id=?", new RecipeMapper(), new Object[] {category});
		} catch (Exception e) {
			throw new DatabaseErrorException();
		}
		return recipes;
	}

	@Override
	public int addRecipe(RecipeModel recipe) {
		KeyHolder key = new GeneratedKeyHolder();
		int result = 0;
		
		try {
			jdbc.update(
					new PreparedStatementCreator() {

						@Override
						public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
							PreparedStatement ps = con.prepareStatement("INSERT INTO recipes (category, recipe_name, recipe_description) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
							ps.setInt(1, recipe.getCategory());
							ps.setString(2, recipe.getRecipeName());
							ps.setString(3, recipe.getRecipeDescription());
							return ps;
						}
						
					},
					key);
			result = jdbc.update("INSERT INTO users_recipes (recipe_id, user_id) VALUES (?,?)",
					key.getKey(), recipe.getUserId());
		} catch (DataIntegrityViolationException e) {
			this.deleteRecipeById(key.getKey().intValue());
			throw new DatabaseErrorException("User does not exist.");
		}
		
		catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		return result;
	}

	@Override
	public RecipeModel updateRecipe(RecipeModel updated) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteRecipeById(int recipe_id) {
		int result = 0;
		
		try {
			result = jdbc.update("DELETE FROM recipes WHERE recipe_id=?", recipe_id);
		} catch (Exception e) {
			throw new DatabaseErrorException();
		}
		return result;
	}

	@Override
	public boolean recipePublish(int recipe_id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int addRecipeStep(RecipeStepModel step) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RecipeStepModel updateRecipeStep(RecipeStepModel updated) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteRecipeStep(int recipe_step_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CategoryModel> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CategoryModel getCategoryById(int category) {
		// TODO Auto-generated method stub
		return null;
	}

}
