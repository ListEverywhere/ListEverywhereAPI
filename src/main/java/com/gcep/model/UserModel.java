package com.gcep.model;

import java.time.LocalDate;

public class UserModel {

	private int user_id;
	private String first_name;
	private String last_name;
	private String email;
	private LocalDate date_of_birth;
	private String username;
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
