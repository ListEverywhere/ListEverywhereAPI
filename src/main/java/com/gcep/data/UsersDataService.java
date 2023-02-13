package com.gcep.data;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.gcep.exception.DatabaseErrorException;
import com.gcep.mapper.UserMapper;
import com.gcep.model.UserModel;

/**
 * Provides the necessary operations for user information
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
@Repository
public class UsersDataService implements UsersDataServiceInterface {
	
	@Autowired
	private PasswordEncoder passwordEncode;
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param dataSource Automatically received from Spring
	 */
	public UsersDataService(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	@Override
	public UserModel getUserById(int id) {
		UserModel user = null;
		// run query to get a user object
		try {
			user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id=?", new UserMapper(), new Object[] {id});
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		
		return user;
	}
	
	@Override
	public List<UserModel> getUsers() {
		List<UserModel> users = new ArrayList<UserModel>();
		// run query to get a list of all users
		try {
			users = jdbcTemplate.query("SELECT * FROM users", new UserMapper());
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		
		return users;
	}

	@Override
	public int addUser(UserModel user) {
		int result = 0;
		
		user.setPassword(passwordEncode.encode(user.getPassword()));
		
		// run query to insert a new user with the given information
		try {
			result = jdbcTemplate.update("INSERT INTO users (first_name, last_name, email, date_of_birth, username, password) VALUES (?,?,?,?,?,?)",
					user.getFirstName(),
					user.getLastName(),
					user.getEmail(),
					user.getDateOfBirth(),
					user.getUsername(),
					user.getPassword());
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		return result;
	}

	@Override
	public boolean deleteUser(int id) {
		int result = 0;
		// run query to delete a user with the given ID
		try {
			result = jdbcTemplate.update("DELETE FROM users WHERE user_id=?", id);
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		return result > 0;
	}

	@Override
	public UserModel updateUser(UserModel updated) {
		int result = 0;
		
		updated.setPassword(passwordEncode.encode(updated.getPassword()));
		
		// run query to update a user with the given information
		try {
			result = jdbcTemplate.update("UPDATE users SET first_name=?, last_name=?, email=?, date_of_birth=?, username=?, password=? WHERE user_id=?",
					updated.getFirstName(),
					updated.getLastName(),
					updated.getEmail(),
					updated.getDateOfBirth(),
					updated.getUsername(),
					updated.getPassword(),
					updated.getId());
		} catch (Exception e) {
			// an error with the database has occurred
			throw new DatabaseErrorException();
		}
		if (result > 0) {
			return updated;
		}
		return null;
	}

	/**
	 * TODO: Remove this method
	 */
	@Override
	public UserModel authenticate(String username, String password) {
		// TODO to be implemented in a future version
		return null;
	}

	@Override
	public UserModel getUserByUsername(String username) {
		UserModel result = null;
		
		try {
			// run query to get a user with the matching username
			// used in UsersService for UserDetailsService
			result = jdbcTemplate.queryForObject("SELECT * FROM users WHERE username=?", new UserMapper(), new Object[] {username});
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage());
		}
		return result;
	}

}
