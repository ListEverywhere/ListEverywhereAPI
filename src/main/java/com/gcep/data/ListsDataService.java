package com.gcep.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.gcep.exception.DatabaseErrorException;
import com.gcep.mapper.CustomListItemMapper;
import com.gcep.mapper.ListItemMapper;
import com.gcep.mapper.ListMapper;
import com.gcep.model.CustomListItemModel;
import com.gcep.model.FoodItemModel;
import com.gcep.model.ItemModel;
import com.gcep.model.ListItemModel;
import com.gcep.model.ListModel;
import com.gcep.model.RecipeItemModel;
import com.gcep.model.RecipeModel;
import com.gcep.service.ItemsService;

/**
 * Provides the necessary operations for shopping list information.
 * @author Gabriel Cepleanu
 * @version 0.2
 *
 */
@Repository
public class ListsDataService implements ListsDataServiceInterface {
	
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ItemsService itemsService;
	
	public ListsDataService(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * Returns a list of all items from a shopping list.
	 * Combines List Items and Custom List Items.
	 * Automatically enables items service for getting item names
	 * @param list
	 * @return
	 */
	private List<ItemModel> getListItems(int list_id) {
		return getListItems(list_id, false, false);
	}
	
	/**
	 * Returns a list of all items from a shopping list.
	 * Combines List Items and Custom List Items.
	 * Provides parameter to disable items service.
	 * @param list The List to find the items for
	 * @param noItemsService If true, disables fetching item names from ItemsService
	 * @return List containing shopping list items
	 */
	private List<ItemModel> getListItems(int list_id, boolean noItemsService, boolean noCustomItems) {
		List<ItemModel> itemList = new ArrayList<ItemModel>();
		// get ListItems
		try {
			// run query to get list items from specified list
			var items = jdbcTemplate.query("SELECT lists.list_id, lists_items.* FROM lists_items "
					+ "INNER JOIN lists ON lists_items.list_id=lists.list_id WHERE lists.list_id=?",
					new ListItemMapper(), new Object[] { list_id });
			
			// skip getting item names if noItemsService is true
			if (!noItemsService) {
				// loop through each list item to get item name
				for (int i = 0; i < items.size(); i++) {
					// use ItemsService to get item details
					FoodItemModel item = itemsService.getItem(items.get(i).getItemId());
					// set the item name from the ItemsService
					items.get(i).setItemName(item.getFood_name());
				}
			}
			
			
			// add each item to the returned item list
			itemList.addAll(items);
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		
		// get CustomListItems
		if (!noCustomItems) {
			try {
				// run query to get custom list items from specified list
				var items = jdbcTemplate.query("SELECT lists.list_id, lists_items_custom.* FROM lists_items_custom "
						+ "INNER JOIN lists ON lists_items_custom.list_id=lists.list_id WHERE lists.list_id=?",
						new CustomListItemMapper(), new Object[] { list_id});
				// add custom list items to returned item list
				itemList.addAll(items);
			} catch (Exception e) {
				throw new DatabaseErrorException(e.getMessage());
			}
		}
		
		
		// sort the item list by position
		itemList.sort((i1, i2) -> Integer.compare(i1.getPosition(), i2.getPosition()));
		
		// return the final item list
		return itemList;
	}

	@Override
	public ListModel getListById(int id) {
		ListModel list = null;
		try {
			// run query to get a list with the matching list_id
			// joins with users_lists to get user_id of list
			list = jdbcTemplate.queryForObject("SELECT lists.*, users_lists.user_id, users_lists.list_id FROM lists "
					+ "INNER JOIN users_lists ON lists.list_id=users_lists.list_id WHERE lists.list_id=?",
					new ListMapper(), new Object[] {id});
			// get all items for the list
			list.setListItems(getListItems(list.getListId()));
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException(e.getMessage());
		}
		return list;
	}

	@Override
	public List<ListModel> getListsByUser(int user_id) {
		// automatically enable items service
		return getListsByUser(user_id, false, false);
	}
	
	/**
	 * Returns a list of all shopping list items by User ID.
	 * @param user_id User ID number
	 * @param noItemsService If true, the list will not contain items
	 * @param noCustomItems If true, the list will not contain custom items
	 * @return List of Shopping Lists
	 */
	public List<ListModel> getListsByUser(int user_id, boolean noItemsService, boolean noCustomItems) {
		List<ListModel> lists = null;
		try {
			// run query to get all lists with a matching user id
			lists = jdbcTemplate.query("SELECT lists.*, users_lists.user_id, users_lists.list_id FROM lists "
					+ "INNER JOIN users_lists ON lists.list_id=users_lists.list_id WHERE users_lists.user_id=?", 
					new ListMapper(), new Object[] {user_id});
			
			// if true, skip getting items
			if (!noItemsService) {
				// populate each list with items
				for (int i = 0; i < lists.size(); i++) {
					var newList = lists.get(i);
					// get the items and item information
					newList.setListItems(getListItems(newList.getListId(), false, noCustomItems));
					// replace unpopulated list with populated list
					lists.set(i, newList);
				}
			}
			
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		
		return lists;
	}

	@Override
	public int createList(ListModel list) {
		
		// used to get the list_id from the first query
		KeyHolder key = new GeneratedKeyHolder();
		
		int result = 0;
		try {
			// first query to update lists table and get list_id
			jdbcTemplate.update(
					new PreparedStatementCreator() {
						// creates the prepared SQL statement and declares the keys to be returned
						@Override
						public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
							PreparedStatement ps = con.prepareStatement("INSERT INTO lists (list_name, creation_date, last_modified) VALUES (?,NOW(),NOW())",
									Statement.RETURN_GENERATED_KEYS);
							ps.setString(1, list.getListName());
							return ps;
						}
						
					}, key);
			// second query to link newly created list with the user id
			result = jdbcTemplate.update("INSERT INTO users_lists (user_id, list_id) VALUES (?,?)",
					list.getUserId(), key.getKey());
		} catch (DataIntegrityViolationException e) {
			// this exception occurs if the first query was successful (create list), but the user id is invalid
			// when this happens, delete the list that was created
			this.deleteListById(key.getKey().intValue());
			throw new DatabaseErrorException("User does not exist.");
			
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		
		return result;
	}

	@Override
	public ListModel updateList(ListModel updated) {
		ListModel retval = null;
		// query to update the list
		try {
			int result = jdbcTemplate.update("UPDATE lists SET list_name=?, last_modified=? WHERE list_id=?", updated.getListName(), updated.getLastModified(), updated.getListId());
			if (result > 0) {
				retval = updated;
			}
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		return retval;
	}

	@Override
	public int deleteListById(int id) {
		int result = 0;
		// query to delete the list
		try {
			result = jdbcTemplate.update("DELETE FROM lists WHERE list_id=?", id);
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		return result;
	}

	@Override
	public int addListItem(int list_id, ItemModel item) {
		int result = 0;
		try {
			int position = -1;
			
			// get the position for the new item
			position = getPositionOfNextItem(list_id);
			
			// check that the position was calculated
			if (position < 0) {
				throw new DatabaseErrorException("Failed to get item position.");
			}
			
			// check what type of item is given so that the appropriate table is updated
			if (item instanceof ListItemModel) {
				// list item, uses ID instead of name
				ListItemModel current = (ListItemModel) item;
				result = jdbcTemplate.update("INSERT INTO lists_items (list_id, item_id, checked, position) VALUES (?,?,false,?)", current.getListId(), current.getItemId(), position);
				
			} else if (item instanceof CustomListItemModel) {
				// custom list item, uses Name instead of ID
				CustomListItemModel current = (CustomListItemModel) item;
				result = jdbcTemplate.update("INSERT INTO lists_items_custom (list_id, item_name, checked, position) VALUES (?,?,false,?)", current.getListId(), current.getItemName(), position);
			}
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		return result;
	}
	
	/**
	 * Returns the next available item position in a given list
	 * @param list_id List ID
	 * @return Next available position
	 */
	public int getPositionOfNextItem(int list_id) {
		int result = -1;
		
		try {
			// SQL query
			// gets the count of the query in parenthesis
			// query selects list items with matching list id, then union with query to select custom list items with matching list id
			// returns the count as an int which is position of new item
			result = jdbcTemplate.queryForObject(
					"SELECT COUNT(*) FROM (SELECT list_item_id, null as custom_item_id FROM lists, lists_items WHERE lists.list_id=? AND lists.list_id=lists_items.list_id UNION SELECT null as list_item_id, custom_item_id FROM lists, lists_items_custom WHERE lists.list_id=? AND lists.list_id=lists_items_custom.list_id) count;"
					, Integer.class, list_id, list_id);
		} catch (Exception e) {
			// an error occurred with the query
			throw new DatabaseErrorException(e.getMessage());
		}
		
		return result;
	}

	@Override
	public ItemModel editListItem(ItemModel updated) {
		ItemModel retval = null;
		int result = 0;
		try {
			// check what type of item is given so that the appropriate table is updated
			if (updated instanceof ListItemModel) {
				ListItemModel current = (ListItemModel) updated;
				
				result = jdbcTemplate.update("UPDATE lists_items SET item_id=?, checked=?, position=? WHERE list_item_id=?",
						current.getItemId(), current.isChecked(), current.getPosition(), current.getListItemId());
				// set retval as updated ItemModel if successful
				if (result > 0) {
					retval = current;
				}
			} else if (updated instanceof CustomListItemModel) {
				CustomListItemModel current = (CustomListItemModel) updated;
				
				result = jdbcTemplate.update("UPDATE lists_items_custom SET item_name=?, checked=?, position=? WHERE custom_item_id=?",
						current.getItemName(), current.isChecked(), current.getPosition(), current.getCustomItemId());
				// set retval as updated ItemModel if successful
				if (result > 0) {
					retval = current;
				}
			}
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException(e.getMessage());
		}
		return retval;
	}
	
	/**
	 * Updates only the position field of a list item.
	 * @param item List/Custom item to update
	 * @return Transaction status
	 */
	private boolean editItemPosition(ItemModel item) {
		int result = 0;
		try {
			// check the type of item
			if (item instanceof ListItemModel) {
				ListItemModel current = (ListItemModel) item;
				
				// run query to update position field
				result = jdbcTemplate.update("UPDATE lists_items SET position=? WHERE list_item_id=?", current.getPosition(), current.getListItemId());
			} else if (item instanceof CustomListItemModel) {
				CustomListItemModel current = (CustomListItemModel) item;
				
				// run query to update position field
				result = jdbcTemplate.update("UPDATE lists_items_custom SET position=? WHERE custom_item_id=?", current.getPosition(), current.getCustomItemId());
			}
		} catch (Exception e) {
			// an error with the database has occured
			throw new DatabaseErrorException(e.getMessage());
		}
		
		return result > 0;
	}
	
	/**
	 * Updates each item position in a given list of item objects
	 * The given item will be inserted at newPosition and each item position will be updated sequentially by 1
	 * @param item Item to be updated
	 * @param list_id List ID to be updated
	 * @param newPosition New position to insert item at
	 * @param swap If enabled, performs swap with item at newPosition instead of insert
	 * @return Transaction status
	 */
	public boolean updateItemPositions(ItemModel item, int list_id, int newPosition, boolean swap) {
		// check if newPosition is valid
		
		if (!isValidPosition(list_id, newPosition)) {
			// position is not valid
			throw new DatabaseErrorException("Invalid list item position.");
		}
		
		// if swap
		if (swap) {
			// SWAP
			
			// get old item at position
			Optional<ItemModel> oldItem = getListItems(list_id, true, false).stream().filter(e -> e.getPosition() == newPosition).findFirst();
			
			// if old item does not exist, return false
			if (oldItem.isEmpty()) {
				return false;
			}
			
			// set old item position to item position
			ItemModel oldItemModel = oldItem.get();
			oldItemModel.setPosition(item.getPosition());
			
			// update old item
			boolean firstResult = editItemPosition(oldItemModel);
			
			// set item position to newPosition
			item.setPosition(newPosition);
			
			// update item
			boolean secondResult = editItemPosition(item);
			
			return firstResult && secondResult;
			
		} else {
			// else
			// ALL ITEMS AFTER
			
			// get the items from the list
			List<ItemModel> items = getListItems(list_id, true, false);
			
			int currentItemPosition = -1;
			int currentItemIndex = -1;
			
			// loop through each item and find the current item that is being moved
			for (int i = 0; i < items.size(); i++) {
				// check if we found the right item
				// if the class is the same type (ListItemModel or CustomListItemModel) and the ID is the same
				if (items.get(i).getClass() == item.getClass() && items.get(i).getPrimaryKey() == item.getPrimaryKey()) {
					// item is found
					currentItemPosition = items.get(i).getPosition();
					currentItemIndex = i;
					break;
				}
			}
			
			// remove the item from the old position
			items.remove(currentItemIndex);
			
			// add the item to the new position
			items.add(newPosition, item);
			
			// update the list item positions
			return updateItemListPositions(0, items);
		}
	}
	
	/**
	 * Checks if a given position in a list is a valid position.
	 * A valid position is a position that is greater than 0 AND,
	 * is currently used by another item OR
	 * is the next available position
	 * @param list_id List ID to be checked
	 * @param position Position to be checked
	 * @return Position is valid
	 */
	private boolean isValidPosition(int list_id, int position) {
		// any position less than 0 is invalid
		if (position < 0) {
			return false;
		}
		
		// get items from list
		List<ItemModel> items = getListItems(list_id, true, false);
		
		// if position is greater than list size -1, invalid position
		if (position > items.size()-1) {
			return false;
		}
		
		// use stream to get item that matches position provided
		Optional<ItemModel> existingItem = items.stream().filter(e -> e.getPosition() == position).findFirst();
		
		// since another item has this position, it is likely also a valid position
		// may only be an issue if an item is given an invalid position beforehand
		if (existingItem.isPresent()) {
			return true;
		}
		
		// use stream again to get an item that has a position before the given position
		existingItem = items.stream().filter(e -> e.getPosition() == position - 1).findFirst();
		
		// if an item exists at this position, it is valid unless it is the same item
		if (existingItem.isPresent()) {
			return true;
		}
		
		return false;
	}

	@Override
	public int deleteListItem(int id) {
		int result = 0;
		// run query to delete a list item
		try {
			ListItemModel item = getListItemDetails(id, true);
			
			if (item != null) {
				result = jdbcTemplate.update("DELETE FROM lists_items WHERE list_item_id=?", id);
				
				// if list items are updated successfully, return result (likely nonzero), otherwise result is 0 for failure
				result = (updateListItemsByPosition(item.getListId(), item.getPosition())) ? result : 0;
			}
			
			
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		return result;
	}
	
	@Override
	public int deleteCustomListItem(int id) {
		int result = 0;
		// run query to remove custom list item
		try {
			CustomListItemModel item = getCustomListItemDetails(id);
			
			if (item!= null) {
				result = jdbcTemplate.update("DELETE FROM lists_items_custom WHERE custom_item_id=?", id);
				
				// if list items are updated successfully, return result (likely nonzero), otherwise result is 0 for failure
				result = (updateListItemsByPosition(item.getListId(), item.getPosition())) ? result : 0;
			}
			
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		return result;
	}
	
	/**
	 * Updates all list items after a given position in a given list
	 * @param list_id ID of the list to be updated
	 * @param startingPosition Starting position for list items
	 * @return Transaction status
	 */
	public boolean updateListItemsByPosition(int list_id, int startingPosition) {
		List<ItemModel> items = getItemsFromListAfterPosition(list_id, startingPosition);
		return updateItemListPositions(startingPosition, items);
	}
	
	/**
	 * Updates item positions sequentially by one given a position and a list of item objects
	 * @param startingPosition Position for starting item
	 * @param items Items to be updated
	 * @return Transaction status
	 */
	private boolean updateItemListPositions(int startingPosition, List<ItemModel> items) {
		int result = 0;
		
		try {
			
			// if no items, skip remaining logic
			if (items.size() == 0) {
				return true;
			}
			
			// loop through each item to update its position
			for (int i = 0; i < items.size(); i++) {
				// get the item from the list of items
				ItemModel item = items.get(i);
				
				// set position to start + the current index (next position)
				item.setPosition(startingPosition+i);
				
				// update the position in the database
				boolean editResult = editItemPosition(item);
				
				// if successful, update result count by 1
				if (editResult) {
					result++;
				}
			}
		} catch (Exception e) {
			// an error with the database has occured
			throw new DatabaseErrorException(e.getMessage());
		}
		
		// return true if every item was successfully updated
		return result == items.size();
	}
	
	/**
	 * Returns a list of item objects after a given position from a list
	 * @param list_id ID number of the list
	 * @param position Starting position
	 * @return List of item objects
	 */
	public List<ItemModel> getItemsFromListAfterPosition(int list_id, int position) {
		List<ItemModel> items = null;
		
		try {
			// get list items with items service disabled as item names are not needed here
			items = getListItems(list_id, true, false);
			
			// remove any items that come before the current item position
			items.removeIf(e -> e.getPosition() <= position);
			
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException(e.getMessage());
		}
		
		return items;
	}

	/**
	 * TODO: Remove this method
	 */
	@Override
	public List<ItemModel> searchItems(String search_term) {
		// TODO to be implemented in a future version
		return null;
	}

	/**
	 * TODO: Remove this method
	 */
	@Override
	public int GetItemById(int item_id) {
		// TODO to be implemented in a future version
		return 0;
	}

	@Override
	public ListItemModel getListItemDetails(int list_item_id, boolean noItemsService) {
		ListItemModel item = null;
		try {
			// get the item
			item = jdbcTemplate.queryForObject("SELECT * FROM lists_items WHERE list_item_id=?", new ListItemMapper(), list_item_id);
			
			// if noItemsService is true, skip getting item name
			if (!noItemsService) {
				// use items service to get item name
				FoodItemModel foodItem = itemsService.getItem(item.getItemId());
				item.setItemName(foodItem.getFood_name());
			}
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException(e.getMessage());
		}
		
		return item;
	}

	@Override
	public CustomListItemModel getCustomListItemDetails(int custom_item_id) {
		CustomListItemModel item = null;
		
		try {
			// get the custom item
			item = jdbcTemplate.queryForObject("SELECT * FROM lists_items_custom WHERE custom_item_id=?", new CustomListItemMapper(), custom_item_id);
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException(e.getMessage());
		}
		return item;
	}
	
	public List<ListItemModel> getAllListItemDetails(List<Integer> listItemIds, boolean noItemsService) {
		List<ListItemModel> items = null;
		
		try {
			items = new ArrayList<ListItemModel>();
			for (int listItemId : listItemIds) {
				items.add(getListItemDetails(listItemId, noItemsService));
			}
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		
		return items;
	}

	@Override
	public int mergeRecipeItemsWithList(int list_id, RecipeModel recipe) {
		int retval = 0;
		
		try {
			// get list items from list id, no custom items
			List<ItemModel> items = getListItems(list_id, true, true);
			
			// get recipe items
			List<RecipeItemModel> recipeItems = recipe.getRecipeItems();
			
			// get the item ids of the list items
			List<Integer> listItemIds = items.stream().map(item -> item.getItemId()).toList();
			
			// remove any item ids from the recipe that are also in the list
			recipeItems = recipeItems.stream().filter(item -> !listItemIds.contains(item.getItemId())).toList();
			
			// add each recipe item to list
			for (RecipeItemModel item : recipeItems) {
				// create new list item
				ListItemModel newItem = new ListItemModel(item.getItemId(), null, false, -1, -1, list_id);
				
				// add item to list
				addListItem(list_id, newItem);
				
			}
			
			retval = 1;
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		
		return retval;
	}

}
