package com.gcep.model;

public class ListItemModel extends ItemModel {
	
	int list_item_id;
	int list_id;
	
	public ListItemModel() {
		super();
	}

	public ListItemModel(int item_id, String item_name, boolean checked, int list_item_id, int list_id) {
		super(item_id, item_name, checked);
		this.list_item_id = list_item_id;
		this.list_id = list_id;
	}

	public int getListItemId() {
		return list_item_id;
	}

	public void setListItemId(int list_item_id) {
		this.list_item_id = list_item_id;
	}

	public int getListId() {
		return list_id;
	}

	public void setListId(int list_id) {
		this.list_id = list_id;
	}
	
	

}
