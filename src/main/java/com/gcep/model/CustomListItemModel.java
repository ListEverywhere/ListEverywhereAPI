package com.gcep.model;

public class CustomListItemModel extends ItemModel {

	int custom_item_id;
	int list_id;
	
	public CustomListItemModel() {
		super();
	}
	
	public CustomListItemModel(int item_id, String item_name, boolean checked, int custom_item_id, int list_id) {
		super(item_id, item_name, checked);
		this.custom_item_id = custom_item_id;
		this.list_id = list_id;
	}

	public int getCustomItemId() {
		return custom_item_id;
	}

	public void setCustomItemId(int custom_item_id) {
		this.custom_item_id = custom_item_id;
	}

	public int getListId() {
		return list_id;
	}

	public void setListId(int list_id) {
		this.list_id = list_id;
	}
	
	
	
	
}
