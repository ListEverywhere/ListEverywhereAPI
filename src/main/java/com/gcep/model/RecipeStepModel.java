package com.gcep.model;

public class RecipeStepModel {
	
	private int recipe_step_id;
	private String step_description;
	private int recipe_id;
	
	public RecipeStepModel() {
		
	}
	
	public RecipeStepModel(int recipe_step_id, String step_description, int recipe_id) {
		super();
		this.recipe_step_id = recipe_step_id;
		this.step_description = step_description;
		this.recipe_id = recipe_id;
	}

	public int getRecipeStepId() {
		return recipe_step_id;
	}

	public void setRecipeStepId(int recipe_step_id) {
		this.recipe_step_id = recipe_step_id;
	}

	public String getStepDescription() {
		return step_description;
	}

	public void setStepDescription(String step_description) {
		this.step_description = step_description;
	}

	public int getRecipeId() {
		return recipe_id;
	}

	public void setRecipeId(int recipe_id) {
		this.recipe_id = recipe_id;
	}
	
	
	
	

}
