package com.gcep.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gcep.data.UsersDataService;
import com.gcep.data.UsersDataServiceInterface;
import com.gcep.model.StatusModel;
import com.gcep.model.UserDetailsModel;
import com.gcep.model.UserModel;
import com.gcep.security.TokenFilter;
import com.gcep.security.TokenUtility;

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
	@Autowired
	private TokenUtility tokenUtility;
	@Autowired
	AuthenticationManager authManager;
	
	/**
	 * Authenticates an existing user with valid credentials and returns a JWT token.
	 * @param user The user credentials (username and password)
	 * @return JSON response
	 */
	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@RequestBody UserModel user) {
		try {
			// use the authentication manager to check provided credentials
			Authentication auth = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			
			// get UserDetails object for the current user
			UserDetailsModel userDetails = (UserDetailsModel)auth.getPrincipal();
			
			// generate a new JWT token
			String token = tokenUtility.generateToken(userDetails);
			
			// return the JWT token for the user
			return new ResponseEntity<>(new StatusModel("token", new String[] {token, Integer.toString(userDetails.getId())}), HttpStatus.OK);
		} catch (Exception e) {
			// credentials are not valid, send error
			return new ResponseEntity<>(new StatusModel("error", e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	private UserDetailsModel getCurrentUser() {
		return (UserDetailsModel)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	@GetMapping("/user")
	public ResponseEntity<?> getDetailsFromToken() {
		UserDetailsModel user = getCurrentUser();
		
		
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	/**
	 * POST method for adding a new user. All fields must be valid.
	 * @param user The user information
	 * @return JSON response
	 */
	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody @Valid UserModel user) {
		// use DAO to register the user
		int result = usersDataService.addUser(user);
		
		if (result > 0) {
			// user was registered
			return new ResponseEntity<>(new StatusModel("success", "User successfully added"), HttpStatus.OK);
		}
		else {
			// failed to register user
			return new ResponseEntity<>(new StatusModel("error", "Error adding user."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * PUT method for updating a specified user. User ID must be specified in the request body.
	 * @param updated The updated user information
	 * @return JSON response
	 */
	@PutMapping("/")
	public ResponseEntity<?> updateUser(@RequestBody UserModel updated) {
		// use DAO to update user information
		UserDetailsModel user = getCurrentUser();
		
		UserModel result = null;
		
		if (updated.getId() == user.getId()) {
			result = usersDataService.updateUser(updated);
		}
		
		if (result != null) {
			// user was updated
			return new ResponseEntity<>(new StatusModel("success", "User successfully updated"), HttpStatus.OK);
		}
		else {
			// failed to update user
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
		UserDetailsModel user = getCurrentUser();
		boolean result = false;
		
		if (user.getId() == id) {
			result = usersDataService.deleteUser(id);
		}
		// use DAO to delete user
		
		if (result) {
			// user was deleted
			return new ResponseEntity<>(new StatusModel("success", "User successfully deleted"), HttpStatus.OK);
		}
		else {
			// failed to delete user
			return new ResponseEntity<>(new StatusModel("error", "Error deleting user."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
