package com.gcep.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcep.data.ListsDataService;
import com.gcep.data.ListsDataServiceInterface;
import com.gcep.model.CustomListItemModel;
import com.gcep.model.FoodItemModel;
import com.gcep.model.ItemModel;
import com.gcep.model.ListItemModel;
import com.gcep.model.ListModel;
import com.gcep.model.StatusModel;
import com.gcep.service.ItemsService;

/**
 * Provides the REST service endpoints for shopping list data operations
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
@RestController
@RequestMapping("/lists")
@CrossOrigin
public class ListsRESTController {
	
	@Autowired
	ListsDataService listsDataService;
	@Autowired
	ItemsService itemsService;
	
	/**
	 * Test method for searching for items using the ItemsService. Will be removed in the future.
	 * @param search_term
	 * @return
	 */
	@GetMapping("/test/{search_term}")
	public ResponseEntity<?> test(@PathVariable(name="search_term") String search_term) {
		List<FoodItemModel> items = null;
		try {
			items = itemsService.searchItems(search_term);
			return new ResponseEntity<>(items,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/test2")
	public ResponseEntity<?> test2() {
		int position = listsDataService.getPositionOfNextItem(25);
		return new ResponseEntity<>(position, HttpStatus.OK);
	}
	
	/**
	 * GET method for getting a list with the given list ID
	 * @param id The ID of the list
	 * @return JSON response
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getListById(@PathVariable(name="id") int id) {
		// use DAO to get the list
		ListModel result = listsDataService.getListById(id);
		
		if (result != null) {
			// list was found
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		else {
			// list does not exist
			return new ResponseEntity<>(new StatusModel("error", "List not found."), HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * GET method for getting a list of all shopping lists from a specified user ID
	 * @param id The ID of the user
	 * @return JSON response
	 */
	@GetMapping("/user/{id}")
	public ResponseEntity<?> getListsByUser(@PathVariable(name="id") int id, @RequestParam(defaultValue="false") boolean noItems) {
		// use DAO to get lists by user id
		List<ListModel> lists = listsDataService.getListsByUser(id, noItems);
		
		if (lists.size() > 0) {
			// one or more lists were found
			return new ResponseEntity<>(lists, HttpStatus.OK);
		}
		else {
			// no lists found
			return new ResponseEntity<>(new StatusModel("error", "No lists were found."), HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * POST method for creating a new shopping list.
	 * This method will ignore any list items specified in the request body.
	 * @param list
	 * @return JSON response
	 */
	@PostMapping("/")
	public ResponseEntity<?> createList(@RequestBody ListModel list) {
		// use DAO to create new list, items ignored
		int result = listsDataService.createList(list);
		
		if (result > 0) {
			// list was created
			return new ResponseEntity<>(new StatusModel("success", "List successfully created."), HttpStatus.OK);
		}
		else {
			// list failed to create
			return new ResponseEntity<>(new StatusModel("error", "There was an error creating a list."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * PUT method for updating a shopping list.
	 * This method will ignore any list items specified in the request body.
	 * @param updated
	 * @return JSON response
	 */
	@PutMapping("/")
	public ResponseEntity<?> updateList(@RequestBody ListModel updated) {
		// use DAO to update list, items ignored
		ListModel retval = listsDataService.updateList(updated);
		
		if (retval != null) {
			// list update was successful
			return new ResponseEntity<>(new StatusModel("success", "List successfully updated."), HttpStatus.OK);
		}
		else {
			// list failed to update
			return new ResponseEntity<>(new StatusModel("error", "There was an error updating the list."), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * DELETE method for removing a shopping list
	 * @param id The ID of the list
	 * @return JSON response
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteList(@PathVariable(name="id") int id) {
		// use DAO to remove list
		int retval = listsDataService.deleteListById(id);
		
		if (retval > 0) {
			// list was deleted
			return new ResponseEntity<>(new StatusModel("success", "List successfully deleted."), HttpStatus.OK);
		}
		else {
			// failed to delete list
			return new ResponseEntity<>(new StatusModel("error", "There was an error deleting the list."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * POST method for adding a list item.
	 * List items are items with an associated item ID.
	 * @param item The list item information
	 * @return JSON response
	 */
	@PostMapping("/items")
	public ResponseEntity<?> addListItem(@RequestBody ListItemModel item) {
		// use DAO to add a new item to a list
		int retval = listsDataService.addListItem(item.getListId(), item);
		
		if (retval > 0) {
			// item was added
			return new ResponseEntity<>(new StatusModel("success", "Item successfully added."), HttpStatus.OK);
		}
		else {
			// failed to add item
			return new ResponseEntity<>(new StatusModel("error", "There was an error adding the item."), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * POST method for adding a custom list item.
	 * Custom list items are items that do not have an item ID, rather the name is given by the user.
	 * @param item The custom list item information
	 * @return JSON response
	 */
	@PostMapping("/items/custom")
	public ResponseEntity<?> addCustomListItem(@RequestBody CustomListItemModel item) {
		// use DAO to add a new custom item to the list (name given by user)
		int retval = listsDataService.addListItem(item.getListId(), item);
		
		if (retval > 0) {
			// custom item was added
			return new ResponseEntity<>(new StatusModel("success", "Item successfully added."), HttpStatus.OK);
		}
		else {
			// failed to add custom item
			return new ResponseEntity<>(new StatusModel("error", "There was an error adding the item."), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * PUT method for updating a list item.
	 * List items are items with an associated item ID.
	 * @param item The updated list item information
	 * @return JSON response
	 */
	@PutMapping("/items")
	public ResponseEntity<?> editListItem(@RequestBody ListItemModel item) {
		// use DAO to update list item
		ItemModel retval = listsDataService.editListItem(item);
		
		if (retval != null) {
			// item was updated
			return new ResponseEntity<>(new StatusModel("success", "List item successfully updated."), HttpStatus.OK);
		}
		else {
			// failed to update item
			return new ResponseEntity<>(new StatusModel("error", "There was an error updating the list item."), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/items/position")
	public ResponseEntity<?> editListItemPosition(@RequestBody ListItemModel item) {
		// use DAO to update list item
		boolean retval = listsDataService.updateItemPositions(item, item.getListId(), item.getPosition(), false);
		
		if (retval) {
			// item was updated
			return new ResponseEntity<>(new StatusModel("success", "List item successfully moved."), HttpStatus.OK);
		}
		else {
			// failed to update item
			return new ResponseEntity<>(new StatusModel("error", "There was an error moving the list item."), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * PUT method for updating a custom list item
	 * Custom list items are items that do not have an item ID, rather the name is given by the user.
	 * @param item The updated custom list item information
	 * @return JSON response
	 */
	@PutMapping("/items/custom")
	public ResponseEntity<?> editCustomListItem(@RequestBody CustomListItemModel item) {
		// use DAO to update custom list item
		ItemModel retval = listsDataService.editListItem(item);
		
		if (retval != null) {
			// custom item was updated
			return new ResponseEntity<>(new StatusModel("success", "Custom List item successfully updated."), HttpStatus.OK);
		}
		else {
			// failed to update custom item
			return new ResponseEntity<>(new StatusModel("error", "There was an error updating the custom list item."), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/items/custom/position")
	public ResponseEntity<?> editCustomListItemPosition(@RequestBody CustomListItemModel item) {
		// use DAO to update list item
		boolean retval = listsDataService.updateItemPositions(item, item.getListId(), item.getPosition(), false);
		
		if (retval) {
			// item was updated
			return new ResponseEntity<>(new StatusModel("success", "List item successfully moved."), HttpStatus.OK);
		}
		else {
			// failed to update item
			return new ResponseEntity<>(new StatusModel("error", "There was an error moving the list item."), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * DELETE method for deleting a list item
	 * List items are items with an associated item ID.
	 * @param id The ID number of the list item entry.
	 * @return JSON response
	 */
	@DeleteMapping("/items/{id}")
	public ResponseEntity<?> removeListItem(@PathVariable(name="id") int id) {
		// use DAO to delete item
		int retval = listsDataService.deleteListItem(id);
		
		if (retval > 0) {
			// item was deleted
			return new ResponseEntity<>(new StatusModel("success", "Item successfully deleted."), HttpStatus.OK);
		}
		else {
			// failed to delete item
			return new ResponseEntity<>(new StatusModel("error", "There was an error deleting the item."), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * DELETE method for deleting a custom list item
	 * Custom list items are items that do not have an item ID, rather the name is given by the user.
	 * @param id The ID number of the custom list item entry.
	 * @return JSON response
	 */
	@DeleteMapping("/items/custom/{id}")
	public ResponseEntity<?> removeCustomListItem(@PathVariable(name="id") int id) {
		// use DAO to delete custom list item
		int retval = listsDataService.deleteCustomListItem(id);
		
		if (retval > 0) {
			// custom item was deleted
			return new ResponseEntity<>(new StatusModel("success", "Item successfully deleted."), HttpStatus.OK);
		}
		else {
			// failed to delete custom item
			return new ResponseEntity<>(new StatusModel("error", "There was an error deleting the item."), HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
