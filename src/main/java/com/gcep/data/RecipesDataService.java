package com.gcep.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.gcep.exception.DatabaseErrorException;
import com.gcep.mapper.CategoryMapper;
import com.gcep.mapper.RecipeItemMapper;
import com.gcep.mapper.RecipeMapper;
import com.gcep.mapper.RecipeStepMapper;
import com.gcep.model.CategoryModel;
import com.gcep.model.FoodItemModel;
import com.gcep.model.RecipeItemModel;
import com.gcep.model.RecipeModel;
import com.gcep.model.RecipeStepModel;
import com.gcep.service.ItemsService;

/**
 * Provides the necessary operations for recipe information.
 * @author Gabriel Cepleanu
 * @version 0.2
 *
 */
@Repository
public class RecipesDataService implements RecipesDataServiceInterface {
	
	@Autowired
	DataSource dataSource;
	JdbcTemplate jdbc;
	
	@Autowired
	private ItemsService itemsService;
	
	public RecipesDataService(DataSource ds) {
		this.dataSource = ds;
		this.jdbc = new JdbcTemplate(ds);
	}
	
	/**
	 * Returns a list of Recipe Steps from the given Recipe object
	 * @param recipe The recipe object
	 * @return List of Recipe Step objects
	 */
	private List<RecipeStepModel> getRecipeSteps(RecipeModel recipe) {
		List<RecipeStepModel> steps = null;
		try {
			// run query to get recipe steps from given recipe
			steps = jdbc.query("SELECT recipes_steps.*, recipes.recipe_id FROM recipes_steps "
					+ "INNER JOIN recipes ON recipes.recipe_id=recipes_steps.recipe_id WHERE recipes.recipe_id=?",
					new RecipeStepMapper(), new Object[] {recipe.getRecipeId()});
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		
		// return the list of steps
		return steps;
	}
	
	/**
	 * Returns a list of Recipe Items from the given Recipe object
	 * @param recipe The recipe object
	 * @return List of Recipe Item objects
	 */
	private List<RecipeItemModel> getRecipeItems(RecipeModel recipe) {
		List<RecipeItemModel> items = null;
		try {
			// run query to get recipe items from given recipe
			items = jdbc.query("SELECT recipes_items.*, recipes.recipe_id FROM recipes_items "
					+ "INNER JOIN recipes ON recipes_items.recipe_id=recipes.recipe_id WHERE recipes_items.recipe_id=?",
					new RecipeItemMapper(), new Object[] { recipe.getRecipeId()});
			
			// go through each item and set item name
			for (int i = 0; i < items.size(); i++) {
				// use ItemsService to get item name
				FoodItemModel item = itemsService.getItem(items.get(i).getItemId());
				// set the item name
				items.get(i).setItemName(item.getFood_name());
			}
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		// return the list of recipe items
		return items;
	}
	
	/**
	 * Takes a list of Recipe Model objects and sets the Recipe Steps and Recipe Items list properties for each Recipe object.
	 * Automatically includes recipe items with object
	 * @param recipes
	 * @return
	 */
	private List<RecipeModel> addStepsItemsToRecipeList(List<RecipeModel> recipes) {
		return addStepsItemsToRecipeList(recipes, false);
	}
	
	/**
	 * Takes a list of Recipe Model objects and sets the Recipe Steps and Recipe Items list properties for each Recipe object.
	 * @param recipes The list of Recipe objects
	 * @param noItems Gets the recipe object without items
	 * @return Updated list of Recipe Objects
	 */
	private List<RecipeModel> addStepsItemsToRecipeList(List<RecipeModel> recipes, boolean noItems) {
		List<RecipeModel> retval = new ArrayList<RecipeModel>();
		
		// for each recipe, populate items and steps
		for (int i = 0; i < recipes.size(); i++) {
			// get the recipe
			var recipe = recipes.get(i);
			// get the steps
			recipe.setRecipeSteps(getRecipeSteps(recipe));
			// get the items
			if (!noItems) {
				recipe.setRecipeItems(getRecipeItems(recipe));
			}
			
			retval.add(recipe);
		}
		
		return retval;
	}

	@Override
	public RecipeModel getRecipeById(int id) {
		RecipeModel recipe = null;
		
		try {
			// run query to get recipe by ID
			recipe = jdbc.queryForObject("SELECT recipes.*, users_recipes.* FROM recipes "
					+ "INNER JOIN users_recipes ON users_recipes.recipe_id=recipes.recipe_id WHERE recipes.recipe_id=?", new RecipeMapper(), new Object[] {id});
			// populate steps
			recipe.setRecipeSteps(getRecipeSteps(recipe));
			// populate items
			recipe.setRecipeItems(getRecipeItems(recipe));
		}
		catch (EmptyResultDataAccessException e) {
			// did not find a recipe, return null
		}
		catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		return recipe;
	}

	@Override
	public List<RecipeModel> getRecipesByUser(int user_id) {
		// automatically enable items service
		return getRecipesByUser(user_id, false);
	}
	
	/**
	 * Returns a list of recipes from a given User ID.
	 * @param user_id The ID number of the user
	 * @param noItems If true, returns recipe object without items
	 * @return List of Recipe objects
	 */
	public List<RecipeModel> getRecipesByUser(int user_id, boolean noItems) {
		List<RecipeModel> recipes = null;
		
		try {
			// run query to get recipes with matching user id
			var recipesInit = jdbc.query("SELECT recipes.*, users_recipes.user_id, users_recipes.recipe_id FROM recipes "
					+ "INNER JOIN users_recipes ON recipes.recipe_id=users_recipes.recipe_id WHERE users_recipes.user_id=?", new RecipeMapper(), new Object[] {user_id});
			// populate items and steps for each recipe
			recipes = addStepsItemsToRecipeList(recipesInit, noItems);
		} catch (Exception e) {
			throw new DatabaseErrorException();
		}
		return recipes;
	}

	@Override
	public List<RecipeModel> getRecipesByCategory(int category) {
		// automatically enable items service
		return getRecipesByCategory(category, false);
	}
		
	/**
	 * Returns a list of recipes from a given category ID.
	 * @param category The ID number of the category.
	 * @param noItems If true, returns recipe object without items
	 * @return List of Recipe objects
	 */
	public List<RecipeModel> getRecipesByCategory(int category, boolean noItems) {
		List<RecipeModel> recipes = null;
		
		try {
			// run query to get recipes with matching category
			var recipesInit = jdbc.query("SELECT recipes.*, categories.category_id, users_recipes.* FROM recipes "
					+ "INNER JOIN categories ON recipes.category=categories.category_id "
					+ "INNER JOIN users_recipes ON users_recipes.recipe_id=recipes.recipe_id WHERE category_id=?", new RecipeMapper(), new Object[] {category});
			// populate items and steps for each recipe
			recipes = addStepsItemsToRecipeList(recipesInit, noItems);
		} catch (Exception e) {
			throw new DatabaseErrorException();
		}
		return recipes;
	}

	@Override
	public int addRecipe(RecipeModel recipe) {
		// initialize key variable (row ID)
		KeyHolder key = new GeneratedKeyHolder();
		int result = 0;
		
		try {
			jdbc.update(
					// create query that returns the key of the newly-created row (PK)
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
			// perform second query to create entry for link between user and recipe
			result = jdbc.update("INSERT INTO users_recipes (recipe_id, user_id) VALUES (?,?)",
					key.getKey(), recipe.getUserId());
		} catch (DataIntegrityViolationException e) {
			// delete the recipe that was created as user does not exist
			this.deleteRecipeById(key.getKey().intValue());
			throw new DatabaseErrorException("User does not exist.");
		}
		
		catch (Exception e) {
			throw new DatabaseErrorException();
		}
		return result;
	}

	@Override
	public RecipeModel updateRecipe(RecipeModel updated) {
		RecipeModel recipe = null;
		try {
			// run query to update recipe information
			int result = jdbc.update("UPDATE recipes SET category=?, recipe_name=?, recipe_description=? WHERE recipe_id=?",
					updated.getCategory(), updated.getRecipeName(), updated.getRecipeDescription(), updated.getRecipeId());
			
			if (result > 0) {
				// recipe was updated, make variable not null
				recipe = updated;
			}
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		return recipe;
	}

	@Override
	public int deleteRecipeById(int recipe_id) {
		int result = 0;
		
		try {
			// run query to delete the recipe
			result = jdbc.update("DELETE FROM recipes WHERE recipe_id=?", recipe_id);
		} catch (Exception e) {
			throw new DatabaseErrorException();
		}
		return result;
	}

	@Override
	public boolean recipePublish(int recipe_id) {
		int result = 0;
		
		try {
			// run query to insert recipe id into published table
			result = jdbc.update("INSERT INTO recipes_published (recipe_id) VALUES (?)", recipe_id);
		} 
		catch (DuplicateKeyException e) {
			// recipe already has entry in the published table, return error
			throw new DatabaseErrorException("This recipe has already been submitted for publishing or is already published.");
		}
		catch (Exception e) {
			throw new DatabaseErrorException();
		}
		// return status of transaction
		return result > 0;
	}

	@Override
	public int addRecipeStep(RecipeStepModel step) {
		int result = 0;
		
		try {
			// run query to add a new recipe step
			result = jdbc.update("INSERT INTO recipes_steps (step_description, recipe_id) VALUES (?,?)",
					step.getStepDescription(), step.getRecipeId());
		} catch (Exception e) {
			throw new DatabaseErrorException();
		}
		return result;
	}

	@Override
	public RecipeStepModel updateRecipeStep(RecipeStepModel updated) {
		RecipeStepModel result = null;
		try {
			// run query to update a recipe step
			int success = jdbc.update("UPDATE recipes_steps SET step_description=? WHERE recipe_step_id=?",
					updated.getStepDescription(), updated.getRecipeStepId());
			if (success > 0) {
				// set variable as not null
				result = updated;
			}
		} catch (Exception e) {
			throw new DatabaseErrorException();
		}
		return result;
	}

	@Override
	public int deleteRecipeStep(int recipe_step_id) {
		int result = 0;
		
		try {
			// run query to delete a recipe step
			result = jdbc.update("DELETE FROM recipes_steps WHERE recipe_step_id=?", recipe_step_id);
		} catch (Exception e) {
			throw new DatabaseErrorException();
		}
		
		return result;
	}

	@Override
	public List<CategoryModel> getCategories() {
		List<CategoryModel> categories = null;
		try {
			// run query to select all categories available
			categories = jdbc.query("SELECT * FROM categories", new CategoryMapper());
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		return categories;
	}

	@Override
	public CategoryModel getCategoryById(int category) {
		CategoryModel retval = null;
		try {
			// run query to return category with matching ID
			retval = jdbc.queryForObject("SELECT * FROM categories WHERE category_id=?", new CategoryMapper(), new Object[] {category});
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		return retval;
	}

	@Override
	public int addRecipeItem(RecipeItemModel item) {
		int result = 0;
		try {
			// run query to add a new recipe item
			result = jdbc.update("INSERT INTO recipes_items (recipe_id, item_id) VALUES (?,?)",
					item.getRecipeId(),
					item.getItemId());
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		return result;
	}

	@Override
	public RecipeItemModel updateRecipeItem(RecipeItemModel updated) {
		RecipeItemModel retval = null;
		try {
			// run query to update a recipe item
			int result = jdbc.update("UPDATE recipes_items SET item_id=? WHERE recipe_item_id=?",
					updated.getItemId(),
					updated.getRecipeItemId());
			// set variable as not null
			if (result > 0) {
				retval = updated;
			}
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		return retval;
	}

	@Override
	public int deleteRecipeItem(int id) {
		int result = 0;
		try {
			// run query to delete a recipe item
			result = jdbc.update("DELETE FROM recipes_items WHERE recipe_item_id=?",
					id);
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		return result;
	}

}
