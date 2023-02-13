package com.gcep.model;

/**
 * Represents a shopping list item that is associated with an item ID number.
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public class ListItemModel extends ItemModel {
	
	int list_item_id;
	int list_id;
	
	public ListItemModel() {
		super();
	}

	/**
	 * 
	 * @param item_id The ID number of the item
	 * @param item_name The name of the item
	 * @param checked Represents if the user marked the item as checked (has received the item)
	 * @param list_item_id The ID number of the item entry
	 * @param list_id The ID number of the list that the item is from
	 */
	public ListItemModel(int item_id, String item_name, boolean checked, int position, int list_item_id, int list_id) {
		super(item_id, item_name, checked, position);
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
