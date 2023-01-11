package com.gcep.model;

import java.util.List;

public class RecipeModel {
	
	private int recipe_id;
	private int category;
	private String recipe_name;
	private String recipe_description;
	private List<RecipeItemModel> recipe_items;
	private List<RecipeStepModel> recipe_steps;
	private boolean published;
	
	public RecipeModel() {
		
	}
	
	public RecipeModel(int recipe_id, int category, String recipe_name, String recipe_description,
			List<RecipeItemModel> recipe_items, List<RecipeStepModel> recipe_steps, boolean published) {
		super();
		this.recipe_id = recipe_id;
		this.category = category;
		this.recipe_name = recipe_name;
		this.recipe_description = recipe_description;
		this.recipe_items = recipe_items;
		this.recipe_steps = recipe_steps;
		this.published = published;
	}

	public int getRecipeId() {
		return recipe_id;
	}

	public void setRecipeId(int recipe_id) {
		this.recipe_id = recipe_id;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getRecipeName() {
		return recipe_name;
	}

	public void setRecipeName(String recipe_name) {
		this.recipe_name = recipe_name;
	}

	public String getRecipeDescription() {
		return recipe_description;
	}

	public void setRecipeDescription(String recipe_description) {
		this.recipe_description = recipe_description;
	}

	public List<RecipeItemModel> getRecipeItems() {
		return recipe_items;
	}

	public void setRecipeItems(List<RecipeItemModel> recipe_items) {
		this.recipe_items = recipe_items;
	}

	public List<RecipeStepModel> getRecipeSteps() {
		return recipe_steps;
	}

	public void setRecipeSteps(List<RecipeStepModel> recipe_steps) {
		this.recipe_steps = recipe_steps;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}
	
	
	
	

}
