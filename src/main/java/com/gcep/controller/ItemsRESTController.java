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

@RestController
@CrossOrigin
@RequestMapping("/items")
public class ItemsRESTController {
	
	@Autowired
	ItemsService itemsService;
	
	@GetMapping("/item/{id}")
	public ResponseEntity<?> getItemById(@PathVariable(name="id") int id) {
		try {
			FoodItemModel item = itemsService.getItem(id);
			return new ResponseEntity<>(item, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new StatusModel("error", "Error getting the item."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/search")
	public ResponseEntity<?> searchItems(@RequestBody SearchModel search) {
		try {
			List<FoodItemModel> items = itemsService.searchItems(search.getSearch());
			return new ResponseEntity<>(items, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new StatusModel("error", "Error getting items."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
