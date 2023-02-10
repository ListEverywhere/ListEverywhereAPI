package com.gcep.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a food item retrieved from the FatSecret Platform API integration.
 * This class only holds the item ID and the item name.
 * @author Gabriel Cepleanu
 * @version 0.1.1
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class FoodItemModel {
	
	private int food_id;
	private String food_name;
	
	/**
	 * Empty FoodItemModel object
	 */
	public FoodItemModel() {
		
	}
	
	/**
	 * 
	 * @param food_id ID number of the food item
	 * @param food_name Name of the food item
	 */
	public FoodItemModel(int food_id, String food_name) {
		super();
		this.food_id = food_id;
		
		if (food_name == null) {
			this.food_name = "Unknown";
		}
		else {
			this.food_name = food_name;
		}
		
		
	}
	
	/**
	 * Returns the food item ID number
	 * @return Food item ID number
	 */
	public int getFood_id() {
		return food_id;
	}

	/**
	 * Sets the ID number of the food item
	 * @param food_id Food item ID number
	 */
	public void setFood_id(int food_id) {
		this.food_id = food_id;
	}

	/**
	 * Returns the name of the food item
	 * @return Food item name
	 */
	public String getFood_name() {
		return food_name;
	}

	/**
	 * Sets the name of the food item
	 * @param food_name Food item name
	 */
	public void setFood_name(String food_name) {
		this.food_name = food_name;
	}
	
	
	
	

}
