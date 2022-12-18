package com.gcep.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gcep.data.ListsDataServiceInterface;
import com.gcep.model.ListModel;
import com.gcep.model.StatusModel;

/**
 * Provides the REST service endpoints for shopping list data operations
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
@RestController
@RequestMapping("/lists")
public class ListsRESTController {
	
	@Autowired
	ListsDataServiceInterface listsDataService;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getListById(@PathVariable(name="id") int id) {
		ListModel result = listsDataService.getListById(id);
		
		if (result != null) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "List not found."), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<?> getListsByUser(@PathVariable(name="id") int id) {
		List<ListModel> lists = listsDataService.getListsByUser(id);
		
		if (lists.size() > 0) {
			return new ResponseEntity<>(lists, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "No lists were found."), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/")
	public ResponseEntity<?> createList(@RequestBody ListModel list) {
		int result = listsDataService.createList(list);
		
		if (result > 0) {
			return new ResponseEntity<>(new StatusModel("success", "List successfully created."), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "There was an error creating a list."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/")
	public ResponseEntity<?> updateList(@RequestBody ListModel updated) {
		ListModel retval = listsDataService.updateList(updated);
		
		if (retval != null) {
			return new ResponseEntity<>(new StatusModel("success", "List successfully updated."), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "There was an error updating the list."), HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
