package com.gcep.model;

/**
 * Represents a base item object. Lists and recipes will use the appropriate item models that extend this class.
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public abstract class ItemModel {
	
	int item_id;
	String item_name;
	boolean checked;
	int position;
	
	public ItemModel() {
		super();
	}

	/**
	 * 
	 * @param item_id The ID number of the item
	 * @param item_name The name of the item
	 * @param checked Represents if the user marked the item as checked (has received the item)
	 */
	public ItemModel(int item_id, String item_name, boolean checked, int position) {
		super();
		this.item_id = item_id;
		this.item_name = item_name;
		this.checked = checked;
		this.position = position;
	}
	
	public abstract int getPrimaryKey();

	public int getItemId() {
		return item_id;
	}

	public void setItemId(int item_id) {
		this.item_id = item_id;
	}

	public String getItemName() {
		return item_name;
	}

	public void setItemName(String item_name) {
		this.item_name = item_name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	

}
