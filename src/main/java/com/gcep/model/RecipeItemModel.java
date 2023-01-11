package com.gcep.model;

public class RecipeItemModel extends ItemModel {
	
	private int recipe_item_id;
	private int recipe_id;
	
	public RecipeItemModel() {
		super();
	}
	
	public RecipeItemModel(int item_id, String item_name, boolean checked, int recipe_item_id, int recipe_id) {
		super(item_id, item_name, checked);
		this.recipe_item_id = recipe_item_id;
		this.recipe_id = recipe_id;
	}

	public int getRecipeItemId() {
		return recipe_item_id;
	}

	public void setRecipeItemId(int recipe_item_id) {
		this.recipe_item_id = recipe_item_id;
	}

	public int getRecipeId() {
		return recipe_id;
	}

	public void setRecipeId(int recipe_id) {
		this.recipe_id = recipe_id;
	}
	
	
	
	
	
	

}
