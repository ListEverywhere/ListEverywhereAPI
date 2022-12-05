package com.gcep.data;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gcep.mapper.UserMapper;
import com.gcep.model.UserModel;

@Repository
public class UsersDataService implements UsersDataServiceInterface {
	
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public UsersDataService(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public UserModel getUserById(int id) {
		UserModel user = null;
		try {
			user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id=?", new UserMapper(), new Object[] {id});
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return user;
	}
	
	@Override
	public List<UserModel> getUsers() {
		List<UserModel> users = new ArrayList<UserModel>();
		try {
			users = jdbcTemplate.query("SELECT * FROM users", new UserMapper());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return users;
	}

	@Override
	public int addUser(UserModel user) {
		int result = 0;
		try {
			result = jdbcTemplate.update("INSERT INTO users (first_name, last_name, email, date_of_birth, username, password) VALUES (?,?,?,?,?,?)",
					user.getFirst_name(),
					user.getLast_name(),
					user.getEmail(),
					user.getDate_of_birth(),
					user.getUsername(),
					user.getPassword());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public boolean deleteUser(int id) {
		int result = 0;
		try {
			result = jdbcTemplate.update("DELETE FROM users WHERE user_id=?", id);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result > 0;
	}

	@Override
	public UserModel updateUser(UserModel updated) {
		int result = 0;
		try {
			result = jdbcTemplate.update("UPDATE users SET first_name=?, last_name=?, email=?, date_of_birth=?, username=?, password=? WHERE user_id=?",
					updated.getFirst_name(),
					updated.getLast_name(),
					updated.getEmail(),
					updated.getDate_of_birth(),
					updated.getUsername(),
					updated.getPassword(),
					updated.getId());
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (result > 0) {
			return updated;
		}
		return null;
	}

	@Override
	public UserModel authenticate(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserModel getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
