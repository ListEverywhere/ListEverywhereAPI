package com.gcep.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gcep.data.RecipesDataServiceInterface;
import com.gcep.model.CategoryModel;
import com.gcep.model.RecipeModel;
import com.gcep.model.StatusModel;

@RestController
@CrossOrigin
@RequestMapping("/recipes")
public class RecipesRESTController {
	
	@Autowired
	RecipesDataServiceInterface recipesDataService;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getRecipeById(@PathVariable(name="id") int id) {
		RecipeModel recipe = recipesDataService.getRecipeById(id);
		
		if (recipe != null) {
			return new ResponseEntity<>(recipe, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "Recipe not found."), HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping("/categories/{id}")
	public ResponseEntity<?> getRecipesByCategory(@PathVariable(name="id") int id) {
		List<RecipeModel> recipes = recipesDataService.getRecipesByCategory(id);
		
		if (recipes != null && recipes.size() > 0) {
			return new ResponseEntity<>(recipes, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "Recipes not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<?> getRecipesByUserId(@PathVariable(name="id") int id) {
		List<RecipeModel> recipes = recipesDataService.getRecipesByUser(id);
		
		if (recipes != null && recipes.size() > 0) {
			return new ResponseEntity<>(recipes, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "Recipes not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/")
	public ResponseEntity<?> createRecipe(@RequestBody RecipeModel recipe) {
		int result = recipesDataService.addRecipe(recipe);
		
		if (result > 0) {
			return new ResponseEntity<>(new StatusModel("success", "Successfully created recipe."), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "Failed to create recipe."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/")
	public ResponseEntity<?> updateRecipe(@RequestBody RecipeModel updated) {
		RecipeModel result = recipesDataService.updateRecipe(updated);
		
		if (result != null) {
			return new ResponseEntity<>(new StatusModel("success", "Successfully updated recipe."), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "Failed to update recipe."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/publish/{id}")
	public ResponseEntity<?> publishRecipe(@PathVariable(name="id") int recipe_id) {
		boolean result = recipesDataService.recipePublish(recipe_id);
		
		if (result) {
			return new ResponseEntity<>(new StatusModel("success", "Recipe was successfully submitted for publishing. Recipe will not be published until it is approved by an administrator."), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "There was an error submitting a recipe for publishing."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/categories/")
	public ResponseEntity<?> getCategories() {
		List<CategoryModel> categories = recipesDataService.getCategories();
		
		if (categories != null && categories.size() > 0) {
			return new ResponseEntity<>(categories, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "No categories found."), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/categories/category/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable(name="id") int id) {
		CategoryModel category = recipesDataService.getCategoryById(id);
		
		if (category != null) {
			return new ResponseEntity<>(category, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "Category not found."), HttpStatus.NOT_FOUND);
		}
	}
	
	

}
