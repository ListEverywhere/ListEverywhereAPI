package com.gcep.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcep.data.RecipesDataService;
import com.gcep.data.RecipesDataServiceInterface;
import com.gcep.model.CategoryModel;
import com.gcep.model.RecipeItemModel;
import com.gcep.model.RecipeModel;
import com.gcep.model.RecipeStepModel;
import com.gcep.model.SearchModel;
import com.gcep.model.StatusModel;

/**
 * Provides the REST service endpoints for recipe data operations
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/recipes")
public class RecipesRESTController {
	
	@Autowired
	RecipesDataService recipesDataService;
	
	/**
	 * Returns a recipe with the given recipe ID
	 * @param id The ID number of the recipe
	 * @return JSON response
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getRecipeById(@PathVariable(name="id") int id) {
		// use DAO to get recipe by id
		RecipeModel recipe = recipesDataService.getRecipeById(id);
		
		if (recipe != null) {
			// recipe was found
			return new ResponseEntity<>(recipe, HttpStatus.OK);
		}
		else {
			// recipe does not exist
			return new ResponseEntity<>(new StatusModel("error", "Recipe not found."), HttpStatus.NOT_FOUND);
		}
		
	}
	
	/**
	 * Returns a list of recipes with the given category ID
	 * @param id The ID numbe of the category
	 * @return JSON response
	 */
	@GetMapping("/categories/{id}")
	public ResponseEntity<?> getRecipesByCategory(@PathVariable(name="id") int id, @RequestParam(defaultValue="false") boolean noItems) {
		// use DAO to return list of recipes with category id
		List<RecipeModel> recipes = recipesDataService.getRecipesByCategory(id, noItems);
		
		if (recipes != null && recipes.size() > 0) {
			// list of recipes is more than 0
			return new ResponseEntity<>(recipes, HttpStatus.OK);
		}
		else {
			// no recipes found
			return new ResponseEntity<>(new StatusModel("error", "Recipes not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Returns a list of recipes with the given user ID
	 * @param id The ID number of the user
	 * @return JSON response
	 */
	@GetMapping("/user/{id}")
	public ResponseEntity<?> getRecipesByUserId(@PathVariable(name="id") int id, @RequestParam(defaultValue="false") boolean noItems) {
		// use DAO to return list of recipes by user id
		List<RecipeModel> recipes = recipesDataService.getRecipesByUser(id, noItems);
		
		if (recipes != null && recipes.size() > 0) {
			// list of recipes is more than 0
			return new ResponseEntity<>(recipes, HttpStatus.OK);
		}
		else {
			// no recipes found
			return new ResponseEntity<>(new StatusModel("error", "Recipes not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/search")
	public ResponseEntity<?> searchRecipesByName(@RequestBody @Valid SearchModel search) {
		List<RecipeModel> foundRecipes = null;
		
		try {
			// use DAO to search for recipes
			foundRecipes = recipesDataService.searchRecipesByName(search);
		} catch (IllegalArgumentException e) {
			// user sent invalid search type
			return new ResponseEntity<>(new StatusModel("error", e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		if (foundRecipes != null) {
			if (foundRecipes.size() > 0) {
				// successfully found recipes
				return new ResponseEntity<>(foundRecipes, HttpStatus.OK);
			}
			// no error with DAO, but no recipes were found
			return new ResponseEntity<>(new StatusModel("error", "No recipes found."), HttpStatus.NOT_FOUND);
		}
		
		// error occurred finding recipes
		return new ResponseEntity<>(new StatusModel("error", "Failed to search recipes."), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Adds a new recipe to the system.
	 * @param recipe The recipe information
	 * @return JSON response
	 */
	@PostMapping("/")
	public ResponseEntity<?> createRecipe(@RequestBody RecipeModel recipe) {
		// use DAO to create new recipe (ignoring items and steps)
		int result = recipesDataService.addRecipe(recipe);
		
		if (result > 0) {
			// recipe was created
			return new ResponseEntity<>(new StatusModel("success", "Successfully created recipe."), HttpStatus.OK);
		}
		else {
			// failed to create recipe
			return new ResponseEntity<>(new StatusModel("error", "Failed to create recipe."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Updates an existing recipe in the system.
	 * @param updated The updated recipe information
	 * @return JSON response
	 */
	@PutMapping("/")
	public ResponseEntity<?> updateRecipe(@RequestBody RecipeModel updated) {
		// use DAO to update recipe (ignores items and steps)
		RecipeModel result = recipesDataService.updateRecipe(updated);
		
		if (result != null) {
			// recipe was updated
			return new ResponseEntity<>(new StatusModel("success", "Successfully updated recipe."), HttpStatus.OK);
		}
		else {
			// failed to update recipe
			return new ResponseEntity<>(new StatusModel("error", "Failed to update recipe."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Removes an existing recipe from the system
	 * @param id The ID number of the recipe
	 * @return JSON response
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteRecipe(@PathVariable(name="id") int id) {
		// use DAO to delete a recipe
		int result = recipesDataService.deleteRecipeById(id);
		
		if (result > 0) {
			// recipe was deleted
			return new ResponseEntity<>(new StatusModel("success", "Successfully deleted recipe."), HttpStatus.OK);
		}
		else {
			// failed to delete recipe
			return new ResponseEntity<>(new StatusModel("error", "Failed to delete recipe."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Marks a recipe for publishing. Recipes will need to be approved by a service administrator.
	 * @param recipe_id The ID number of the recipe.
	 * @return JSON response
	 */
	@PostMapping("/publish/{id}")
	public ResponseEntity<?> publishRecipe(@PathVariable(name="id") int recipe_id) {
		// use DAO to mark a recipe for publishing
		boolean result = recipesDataService.recipePublish(recipe_id);
		
		if (result) {
			// recipe was marked for publishing, but is not yet approved
			return new ResponseEntity<>(new StatusModel("success", "Recipe was successfully submitted for publishing. Recipe will not be published until it is approved by an administrator."), HttpStatus.OK);
		}
		else {
			// recipe is already marked for publishing or is already published
			return new ResponseEntity<>(new StatusModel("error", "There was an error submitting a recipe for publishing."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Returns a list of categories
	 * @return JSON response
	 */
	@GetMapping("/categories/")
	public ResponseEntity<?> getCategories() {
		// use DAO to get list of all categories
		List<CategoryModel> categories = recipesDataService.getCategories();
		
		if (categories != null && categories.size() > 0) {
			// categories found
			return new ResponseEntity<>(categories, HttpStatus.OK);
		}
		else {
			// no categories available
			return new ResponseEntity<>(new StatusModel("error", "No categories found."), HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Returns category information by the given ID
	 * @param id The ID number of the category
	 * @return JSON response
	 */
	@GetMapping("/categories/category/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable(name="id") int id) {
		// use DAO to get category information
		CategoryModel category = recipesDataService.getCategoryById(id);
		
		if (category != null) {
			// category was found
			return new ResponseEntity<>(category, HttpStatus.OK);
		}
		else {
			// category does not exist
			return new ResponseEntity<>(new StatusModel("error", "Category not found."), HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Adds a new recipe step
	 * @param step The recipe step information
	 * @return JSON response
	 */
	@PostMapping("/steps/")
	public ResponseEntity<?> addRecipeStep(@RequestBody RecipeStepModel step) {
		// use DAO to add a new recipe step
		int result = recipesDataService.addRecipeStep(step);
		
		if (result > 0) {
			// recipe step was added
			return new ResponseEntity<>(new StatusModel("success", "Successfully added step."), HttpStatus.OK);
		}
		else {
			// failed to add recipe step
			return new ResponseEntity<>(new StatusModel("error", "Failed to create step."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Updates an existing recipe step
	 * @param updated The updated recipe step information
	 * @return JSON response
	 */
	@PutMapping("/steps/")
	public ResponseEntity<?> updateRecipeStep(@RequestBody RecipeStepModel updated) {
		// use DAO to update a recipe step
		RecipeStepModel result = recipesDataService.updateRecipeStep(updated);
		
		if (result != null) {
			// recipe step was updated
			return new ResponseEntity<>(new StatusModel("success", "Successfully updated step."), HttpStatus.OK);
		}
		else {
			// failed to update recipe step
			return new ResponseEntity<>(new StatusModel("error", "Failed to update step."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Removes an existing recipe step
	 * @param id ID number of the recipe step entry
	 * @return JSON response
	 */
	@DeleteMapping("/steps/{id}") 
	public ResponseEntity<?> deleteRecipeStep(@PathVariable(name="id") int id) {
		// use DAO to delete recipe step
		int result = recipesDataService.deleteRecipeStep(id);
		
		if (result > 0) {
			// recipe step was deleted
			return new ResponseEntity<>(new StatusModel("success", "Successfully deleted step."), HttpStatus.OK);
		}
		else {
			// failed to delete recipe step
			return new ResponseEntity<>(new StatusModel("error", "Failed to delete step."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Adds a new recipe item
	 * @param item The recipe item information
	 * @return JSON response
	 */
	@PostMapping("/items/")
	public ResponseEntity<?> addRecipeItem(@RequestBody RecipeItemModel item) {
		// use DAO to add new recipe item
		int result = recipesDataService.addRecipeItem(item);
		
		if (result > 0) {
			// recipe item was added
			return new ResponseEntity<>(new StatusModel("success", "Recipe item was successfully added"), HttpStatus.OK);
		}
		else {
			// failed to add recipe item
			return new ResponseEntity<>(new StatusModel("error", "Failed to add recipe item."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Updates an existing recipe item
	 * @param updated The updated recipe item information
	 * @return JSON response
	 */
	@PutMapping("/items/")
	public ResponseEntity<?> updateRecipeItem(@RequestBody RecipeItemModel updated) {
		// use DAO to update recipe item
		RecipeItemModel result = recipesDataService.updateRecipeItem(updated);
		
		if (result != null) {
			// recipe item was updated
			return new ResponseEntity<>(new StatusModel("success", "Recipe item was successfully updated"), HttpStatus.OK);
		}
		else {
			// failed to update recipe item
			return new ResponseEntity<>(new StatusModel("error", "Failed to updated recipe item."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Removes an existing recipe item
	 * @param id The ID number of the recipe item entry
	 * @return JSON response
	 */
	@DeleteMapping("/items/{id}")
	public ResponseEntity<?> deleteRecipeItem(@PathVariable(name="id") int id) {
		// use DAO to delete recipe item
		int result = recipesDataService.deleteRecipeItem(id);
		
		if (result > 0) {
			// recipe item was deleted
			return new ResponseEntity<>(new StatusModel("success", "Recipe item was successfully deleted"), HttpStatus.OK);
		}
		else {
			// failed to delete recipe item
			return new ResponseEntity<>(new StatusModel("error", "Failed to delete recipe item."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	

}
