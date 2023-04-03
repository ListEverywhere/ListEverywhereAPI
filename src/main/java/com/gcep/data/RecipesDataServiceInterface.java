package com.gcep.data;
import java.util.List;

import com.gcep.model.CategoryModel;
import com.gcep.model.ListItemModel;
import com.gcep.model.RecipeItemModel;
import com.gcep.model.RecipeModel;
import com.gcep.model.RecipeStepModel;
import com.gcep.model.SearchModel;

/**
 * Provides the outline of methods for various operations performed with recipes data.
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public interface RecipesDataServiceInterface {
	
	/**
	 * Returns a Recipe with the given Recipe ID
	 * @param id The ID number of the recipe
	 * @return Recipe object
	 */
	public RecipeModel getRecipeById(int id);
	/**
	 * Returns a list of recipes from a given User ID.
	 * @param user_id The ID number of the user
	 * @return List of Recipe objects
	 */
	public List<RecipeModel> getRecipesByUser(int user_id);
	/**
	 * Returns a list of recipes from a given category ID.
	 * @param category The ID number of the category.
	 * @return List of Recipe objects
	 */
	public List<RecipeModel> getRecipesByCategory(int category);
	/**
	 * Adds a new recipe to the system
	 * @param recipe The recipe information
	 * @return Status (1 if successful)
	 */
	public int addRecipe(RecipeModel recipe);
	/**
	 * Updates an existing recipe in the system.
	 * @param updated The updated recipe information
	 * @return Status (1 if successful)
	 */
	public RecipeModel updateRecipe(RecipeModel updated);
	/**
	 * Removes an existing recipe from the system with the given Recipe ID.
	 * @param recipe_id The ID number of the recipe.
	 * @return The given RecipeModel object if successful, otherwise null
	 */
	public int deleteRecipeById(int recipe_id);
	/**
	 * Marks a recipe for publishing.
	 * Recipe is not published until it is approved by the service administrator.
	 * @param recipe_id The ID number of the recipe.
	 * @return Status
	 */
	public boolean recipePublish(int recipe_id);
	/**
	 * Adds a step to an existing recipe
	 * @param step The recipe step information
	 * @return Status (1 if successful)
	 */
	public int addRecipeStep(RecipeStepModel step);
	/**
	 * Updates an existing step
	 * @param updated The updated recipe step information
	 * @return The given RecipeStepModel object if successful, otherwise null
	 */
	public RecipeStepModel updateRecipeStep(RecipeStepModel updated);
	/**
	 * Removes an existing step
	 * @param recipe_step_id The ID number of the recipe step entry
	 * @return Status (1 if successful)
	 */
	public int deleteRecipeStep(int recipe_step_id);
	/**
	 * Returns a list of all categories in the system
	 * @return List of Category objects
	 */
	public List<CategoryModel> getCategories();
	/**
	 * Gets a specified category by ID number
	 * @param category The ID number of the category
	 * @return Category object
	 */
	public CategoryModel getCategoryById(int category);
	/**
	 * Adds an item to an existing recipe
	 * @param item The recipe item information
	 * @return Status (1 if successful)
	 */
	public int addRecipeItem(RecipeItemModel item);
	/**
	 * Updates an existing recipe item entry
	 * @param updated The updated recipe item information
	 * @return The given RecipeItemModel object if successful, otherwise null
	 */
	public RecipeItemModel updateRecipeItem(RecipeItemModel updated);
/**
 * Removes an existing recipe item entry
 * @param id The ID number of the recipe item entry
 * @return Status (1 if successful)
 */
	public int deleteRecipeItem(int id);
	
	/**
	 * Returns a list of recipes with a name matching the search term.
	 * If search type is not specified, search defaults to contains
	 * @param search Search query
	 * @return List of Recipe objects
	 */
	public List<RecipeModel> searchRecipesByName(SearchModel search);
	
	/**
	 * Returns a list of recipes that contain the item ids from list_item_ids. Custom items are not supported.
	 * list_item_ids should contain the primary keys for list items
	 * @param list_item_ids The ID numbers of list item entries
	 * @return List of Recipe objects
	 */
	public List<RecipeModel> searchRecipesByListItems(List<ListItemModel> listItems);

}
