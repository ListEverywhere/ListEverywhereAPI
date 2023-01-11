package com.gcep.data;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
			recipe = jdbc.queryForObject("SELECT * FROM recipes WHERE recipe_id=?", new RecipeMapper(), new Object[] {id});
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
					+ "INNER JOIN users_recipes ON recipes.recipe_id=users_recipes.recipe_id WHERE user_id=?", new RecipeMapper(), new Object[] {user_id});
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		return recipes;
	}

	@Override
	public List<RecipeModel> getRecipesByCategory(int category) {
		List<RecipeModel> recipes = null;
		
		try {
			recipes = jdbc.query("SELECT recipes.*, categories.category_id FROM recipes "
					+ "INNER JOIN categories ON recipes.category=categories.category_id WHERE category_id=?", new RecipeMapper(), new Object[] {category});
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		return recipes;
	}

	@Override
	public int addRecipe(RecipeModel recipe) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RecipeModel updateRecipe(RecipeModel updated) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteRecipeById(int recipe_id) {
		// TODO Auto-generated method stub
		return 0;
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
