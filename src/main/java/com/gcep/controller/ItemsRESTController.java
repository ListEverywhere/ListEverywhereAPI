package com.gcep.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gcep.model.FoodItemModel;
import com.gcep.model.SearchModel;
import com.gcep.model.StatusModel;
import com.gcep.service.ItemsService;

/**
 * Provides the REST service for getting item information and searching items using the FatSecret Platform API.
 * @author Gabriel Cepleanu
 * @version 0.2
 */
@RestController
@CrossOrigin
@RequestMapping("/items")
public class ItemsRESTController {
	
	@Autowired
	ItemsService itemsService;
	
	/**
	 * Gets the item name for the given ID number
	 * @param id Item ID number
	 * @return JSON response
	 */
	@GetMapping("/item/{id}")
	public ResponseEntity<?> getItemById(@PathVariable(name="id") int id) {
		try {
			// use items service to get item
			FoodItemModel item = itemsService.getItem(id);
			// return item data
			return new ResponseEntity<>(item, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new StatusModel("error", "Error getting the item."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	/**
	 * Searches for a list of items for the given search term
	 * Currently only returns the first 10 results.
	 * @param search Search term
	 * @return JSON response
	 */
	@PostMapping("/search")
	public ResponseEntity<?> searchItems(@RequestBody SearchModel search) {
		try {
			// use items service to search for the item
			List<FoodItemModel> items = itemsService.searchItems(search.getSearch());
			// return list of items
			return new ResponseEntity<>(items, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new StatusModel("error", "Error getting items."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
