package com.gcep.model;

/**
 * Represents an item object for recipes.
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public class RecipeItemModel extends ItemModel {
	
	private int recipe_item_id;
	private int recipe_id;
	
	public RecipeItemModel() {
		super();
	}
	
	/**
	 * 
	 * @param item_id The ID number of the item
	 * @param item_name The name of the item
	 * @param checked If the item is checked (not used in a recipe)
	 * @param recipe_item_id The ID number of the recipe item entry
	 * @param recipe_id The ID number of the recipe that the item is from
	 */
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
