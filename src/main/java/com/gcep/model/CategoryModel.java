package com.gcep.model;

/**
 * This class represents a recipe Category object.
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public class CategoryModel {
	
	private int category_id;
	private String category_name;
	
	public CategoryModel() {
		
	}
	
	/**
	 * 
	 * @param category_id The ID number of the category
	 * @param category_name The name of the category
	 */
	public CategoryModel(int category_id, String category_name) {
		super();
		this.category_id = category_id;
		this.category_name = category_name;
	}

	public int getCategoryId() {
		return category_id;
	}

	public void setCategoryId(int category_id) {
		this.category_id = category_id;
	}

	public String getCategoryName() {
		return category_name;
	}

	public void setCategoryName(String category_name) {
		this.category_name = category_name;
	}
	
	
	
	

}
