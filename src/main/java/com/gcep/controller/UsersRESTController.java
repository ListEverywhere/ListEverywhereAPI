package com.gcep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gcep.data.UsersDataServiceInterface;
import com.gcep.model.StatusModel;
import com.gcep.model.UserModel;

@RestController
@RequestMapping("/users")
public class UsersRESTController {
	
	@Autowired
	UsersDataServiceInterface usersDataService;
	
	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody UserModel user) {
		int result = usersDataService.addUser(user);
		
		if (result > 0) {
			return new ResponseEntity<>(new StatusModel("success", "User successfully added"), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new StatusModel("error", "Error adding user."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getUsers() {
		return new ResponseEntity<>(usersDataService.getUsers(), HttpStatus.OK);
	}
	
}
