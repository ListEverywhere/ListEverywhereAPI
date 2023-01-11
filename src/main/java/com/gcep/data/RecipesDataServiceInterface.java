package com.gcep.data;
import java.util.List;

import com.gcep.model.CategoryModel;
import com.gcep.model.RecipeModel;
import com.gcep.model.RecipeStepModel;

public interface RecipesDataServiceInterface {
	
	public RecipeModel getRecipeById(int id);
	public List<RecipeModel> getRecipesByUser(int user_id);
	public List<RecipeModel> getRecipesByCategory(int category);
	public int addRecipe(RecipeModel recipe);
	public RecipeModel updateRecipe(RecipeModel updated);
	public int deleteRecipeById(int recipe_id);
	public boolean recipePublish(int recipe_id);
	public int addRecipeStep(RecipeStepModel step);
	public RecipeStepModel updateRecipeStep(RecipeStepModel updated);
	public int deleteRecipeStep(int recipe_step_id);
	public List<CategoryModel> getCategories();
	public CategoryModel getCategoryById(int category);

}
