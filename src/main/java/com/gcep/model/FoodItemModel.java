package com.gcep.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FoodItemModel {
	
	private int food_id;
	private String food_name;
	
	public FoodItemModel() {
		
	}
	
	public FoodItemModel(int food_id, String food_name) {
		super();
		this.food_id = food_id;
		this.food_name = food_name;
	}

	public int getFood_id() {
		return food_id;
	}

	public void setFood_id(int food_id) {
		this.food_id = food_id;
	}

	public String getFood_name() {
		return food_name;
	}

	public void setFood_name(String food_name) {
		this.food_name = food_name;
	}
	
	
	
	

}
