package com.gcep.data;

import java.util.List;

import com.gcep.model.UserModel;

/**
 * Provides the methods for various operations performed with user data.
 * @author gabriel
 *
 */
public interface UsersDataServiceInterface {
	/**
	 * Gets a user with the specified ID number
	 * @param id The ID of the user
	 * @return User information
	 */
	public UserModel getUserById(int id);
	/**
	 * Gets a list of all users
	 * @return All users and the user information
	 */
	public List<UserModel> getUsers();
	/**
	 * Adds a new user to the system
	 * @param user The valid user information
	 * @return Status of the operation. If 1, user was successfully added.
	 */
	public int addUser(UserModel user);
	/**
	 * Removes a specified user from the system
	 * @param id The ID number of the user
	 * @return Status of the operation. If 1, user was successfully deleted.
	 */
	public boolean deleteUser(int id);
	/**
	 * Updates a specified user in the system
	 * @param updated The updated user information
	 * @return If successful, the updated user information is returned.
	 */
	public UserModel updateUser(UserModel updated);
	/**
	 * Authenticates a given user credentials
	 * @param username The username of the user
	 * @param password The password of the user
	 * @return Returns a user object if the credentials are correct.
	 */
	public UserModel authenticate(String username, String password);
	/**
	 * Returns a specific user that matches the given username
	 * @param username The username of the user
	 * @return If successful, the specified user model is returned.
	 */
	public UserModel getUserByUsername(String username);
	
}
