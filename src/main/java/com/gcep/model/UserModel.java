package com.gcep.model;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserModel {

	private int user_id;
	
	@NotNull
	@Size(min=2, max=25, message="First Name must be between 2 to 25 characters.")
	private String first_name;
	
	@NotNull
	@Size(min=2, max=25, message="Last Name must be between 2 to 25 characters.")
	private String last_name;
	
	@NotNull
	@Size(min=4, max=40, message="Email Address must be between 4 to 40 characters.")
	private String email;
	
	@NotNull
	private LocalDate date_of_birth;
	
	@NotNull
	@Size(min=5, max=20, message="Username must be between 5 to 20 characters.")
	private String username;
	
	@NotNull
	@Size(min=8, max=32, message="Password must be between 8 to 32 characters.")
	private String password;
	
	public UserModel() {
		
	}
	
	public UserModel(int user_id, String first_name, String last_name, String email, LocalDate date_of_birth,
			String username, String password) {
		super();
		this.user_id = user_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.date_of_birth = date_of_birth;
		this.username = username;
		this.password = password;
	}


	public int getId() {
		return user_id;
	}
	public void setId(int user_id) {
		this.user_id = user_id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(LocalDate date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "UserModel [user_id=" + user_id + ", first_name=" + first_name + ", last_name=" + last_name + ", email="
				+ email + ", date_of_birth=" + date_of_birth + ", username=" + username + ", password=" + password
				+ "]";
	}
	
	
}
