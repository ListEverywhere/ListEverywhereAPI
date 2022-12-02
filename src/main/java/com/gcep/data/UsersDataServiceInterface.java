package com.gcep.data;

import java.util.List;

import com.gcep.model.UserModel;

public interface UsersDataServiceInterface {
	public UserModel getUserById(int id);
	public List<UserModel> getUsers();
	public int addUser(UserModel user);
	public boolean deleteUser(int id);
	public UserModel updateUser(UserModel updated);
	public UserModel authenticate(String username, String password);
	public UserModel getUserByUsername(String username);
	
}
