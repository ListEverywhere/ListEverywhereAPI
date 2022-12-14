package com.gcep.controller;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RestController;

import com.gcep.data.UsersDataService;
import com.gcep.data.UsersDataServiceInterface;
import com.gcep.model.StatusModel;
import com.gcep.model.UserModel;

/**
 * Provides the REST service endpoints for user data operations
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UsersRESTController {
	
	@Autowired
	UsersDataService usersDataService;
	
	/**
	 * POST method for adding a new user. All fields must be valid.
	 * @param user The user information
	 * @return JSON response
	 */
	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody @Valid UserModel user) {
		int result = usersDataService.addUser(user);
		
		if (result > 0) {
			return new ResponseEntity<>(new StatusModel("success", "User successfully added"), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "Error adding user."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * GET method for returning a list of all users.
	 * @return JSON response
	 */
	@GetMapping("/")
	public ResponseEntity<?> getUsers() {
		return new ResponseEntity<>(usersDataService.getUsers(), HttpStatus.OK);
	}
	
	/**
	 * PUT method for updating a specified user. User ID must be specified in the request body.
	 * @param updated The updated user information
	 * @return JSON response
	 */
	@PutMapping("/")
	public ResponseEntity<?> updateUser(@RequestBody UserModel updated) {
		UserModel result = usersDataService.updateUser(updated);
		
		if (result != null) {
			return new ResponseEntity<>(new StatusModel("success", "User successfully updated"), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "Error updating user."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * DELETE method for removing a specified user.
	 * @param id The ID of the user
	 * @return JSON response
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable(name="id") int id) {
		boolean result = usersDataService.deleteUser(id);
		
		if (result) {
			return new ResponseEntity<>(new StatusModel("success", "User successfully deleted"), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "Error deleting user."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
