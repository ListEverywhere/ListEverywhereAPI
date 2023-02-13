package com.gcep.model;

/**
 * Represents a shopping list item with a custom name. Custom items do not have a matching item ID number.
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public class CustomListItemModel extends ItemModel {

	int custom_item_id;
	int list_id;
	
	public CustomListItemModel() {
		super();
	}
	
	/**
	 * 
	 * @param item_id The ID number of the item
	 * @param item_name The Name of the item
	 * @param checked Represents if the user marked the item as checked (has received the item)
	 * @param custom_item_id The ID number of the custom item entry
	 * @param list_id The ID number of the list that the item is from
	 */
	public CustomListItemModel(int item_id, String item_name, boolean checked, int position, int custom_item_id, int list_id) {
		super(item_id, item_name, checked, position);
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
