package com.gcep.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gcep.data.RecipesDataServiceInterface;
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

}
