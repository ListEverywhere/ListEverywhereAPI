package com.gcep.data;

import java.util.List;

import com.gcep.model.CustomListItemModel;
import com.gcep.model.ItemModel;
import com.gcep.model.ListItemModel;
import com.gcep.model.ListModel;
import com.gcep.model.RecipeModel;

/**
 * Provides the outline of methods for various operations performed with shopping list data.
 * @author Gabriel Cepleanu
 * @version 0.2
 *
 */
public interface ListsDataServiceInterface {
	/**
	 * Returns a shopping list with the given ID
	 * @param id The List ID
	 * @return List information
	 */
	public ListModel getListById(int id);
	/**
	 * Returns a list of all shopping lists from the given user ID
	 * @param user_id The ID of the user
	 * @return List of Shopping Lists
	 */
	public List<ListModel> getListsByUser(int user_id);
	/**
	 * Adds a new list to the system. Does not affect list items.
	 * @param list The new shopping list
	 * @return Status of operation. If 1, operation was successful.
	 */
	public int createList(ListModel list);
	/**
	 * Updates an existing list in the system. Does not affect list items.
	 * @param updated The updated shopping list
	 * @return If successful, updated ListModel is returned.
	 */
	public ListModel updateList(ListModel updated);
	/**
	 * Removes a shopping list with the given list ID
	 * @param id The ID of the list
	 * @return Status of operation. If 1, operation was successful.
	 */
	public int deleteListById(int id);
	/**
	 * Adds a new item to a shopping list.
	 * @param list_id The ID number of the list to append to.
	 * @param item The item information
	 * @return Status of operation. If 1, operation was successful.
	 */
	public int addListItem(int list_id, ItemModel item);
	/**
	 * Updates an existing item.
	 * @param updated The updated item information
	 * @return If successful, updated ItemModel is returned.
	 */
	public ItemModel editListItem(ItemModel updated);
	/**
	 * Deletes an item from a shopping list.
	 * @param id List Item ID number of the item to remove
	 * @return Status of operation. If 1, operation was successful.
	 */
	public int deleteListItem(int id);
	/**
	 * Deletes a custom list item from a shopping list.
	 * @param id Custom List Item ID number of the item to remove
	 * @return Status of operation. If 1, operation was successful.
	 */
	public int deleteCustomListItem(int id);
	/**
	 * Searches for items containing the search term in the item name.
	 * @param search_term The item name (contains, case insensitive)
	 * @return A list of matching items.
	 */
	public List<ItemModel> searchItems(String search_term);
	/**
	 * Gets an item information with the specified ID
	 * @param item_id The ID of the item
	 * @return Item information
	 */
	public int GetItemById(int item_id);
	
	/**
	 * Gets a single list item information with the specified ID
	 * @param list_item_id ID number of the list item entry
	 * @param noItemsService If true, disable getting the item name
	 * @return List Item object
	 */
	public ListItemModel getListItemDetails(int list_item_id, boolean noItemsService);
	
	/**
	 * Gets a single custom list item information with the specified ID
	 * @param custom_item_id ID number of the custom list item entry
	 * @return Custom List Item object
	 */
	public CustomListItemModel getCustomListItemDetails(int custom_item_id);
	
	/**
	 * Combines items from a recipe into a specified shopping list without duplicates.
	 * @param list_id ID number of the list
	 * @param recipe_id ID number of the recipe
	 * @return Status of operation. If 1, operation was successful.
	 */
	public int mergeRecipeItemsWithList(int list_id, RecipeModel recipe);
}
