package com.gcep.model;

public abstract class ItemModel {
	
	int item_id;
	String item_name;
	boolean checked;
	
	public ItemModel() {
		super();
	}

	public ItemModel(int item_id, String item_name, boolean checked) {
		super();
		this.item_id = item_id;
		this.item_name = item_name;
		this.checked = checked;
	}

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
	
	

}
