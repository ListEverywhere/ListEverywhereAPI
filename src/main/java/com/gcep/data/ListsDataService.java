package com.gcep.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.gcep.model.ItemModel;
import com.gcep.model.ListModel;

/**
 * Provides the necessary operations for shopping list information.
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
@Repository
public class ListsDataService implements ListsDataServiceInterface {
	
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public ListsDataService(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * Returns a list of all items from a shopping list.
	 * Combines List Items and Custom List Items.
	 * @param list The List to find the items for
	 * @return List containing shopping list items
	 */
	private List<ItemModel> getListItems(ListModel list) {
		List<ItemModel> itemList = new ArrayList<ItemModel>();
		// get ListItems
		try {
			var items = jdbcTemplate.query("SELECT lists.list_id, lists_items.* FROM lists_items "
					+ "INNER JOIN lists ON lists_items.list_id=lists.list_id WHERE lists.list_id=?",
					new ListItemMapper(), new Object[] { list.getListId()});
			itemList.addAll(items);
		} catch (Exception e) {
			throw new DatabaseErrorException();
		}
		
		// get CustomListItems
		try {
			var items = jdbcTemplate.query("SELECT lists.list_id, lists_items_custom.* FROM lists_items_custom "
					+ "INNER JOIN lists ON lists_items_custom.list_id=lists.list_id WHERE lists.list_id=?",
					new CustomListItemMapper(), new Object[] { list.getListId()});
			itemList.addAll(items);
		} catch (Exception e) {
			throw new DatabaseErrorException();
		}
		
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
			list.setListItems(getListItems(list));
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		return list;
	}

	@Override
	public List<ListModel> getListsByUser(int user_id) {
		List<ListModel> lists = null;
		try {
			lists = jdbcTemplate.query("SELECT lists.*, users_lists.user_id, users_lists.list_id FROM lists "
					+ "INNER JOIN users_lists ON lists.list_id=users_lists.list_id WHERE users_lists.user_id=?", 
					new ListMapper(), new Object[] {user_id});
			// get list items for each list returned
			for (int i = 0; i < lists.size(); i++) {
				var newList = lists.get(i);
				newList.setListItems(getListItems(newList));
				lists.set(i, newList);
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemModel editListItem(ItemModel updated) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteListItem(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ItemModel> searchItems(String search_term) {
		// TODO to be implemented in a future version
		return null;
	}

	@Override
	public int GetItemById(int item_id) {
		// TODO to be implemented in a future version
		return 0;
	}

}
