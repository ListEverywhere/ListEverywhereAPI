package com.gcep.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gcep.model.UserModel;

@Repository
public class TempUsersDataService implements UsersDataServiceInterface {
	
	List<UserModel> users;
	
	public TempUsersDataService() {
		users = new ArrayList<UserModel>();
		users.add(new UserModel(1, "Bob", "Smith", "bobsmith@gmail.com", LocalDate.now(), "username", "password"));
		users.add(new UserModel(2, "Jane", "Badger", "jane456@gmail.com", LocalDate.now(), "jbad", "12345678"));
		users.add(new UserModel(3, "Kroger", "Inc", "kroger@gmail.com", LocalDate.now(), "kroger", "frysfood"));
	}

	@Override
	public UserModel getUserById(int id) {
		for (UserModel userModel : users) {
			if (userModel.getId() == id) {
				return userModel;
			}
		}
		return null;
	}

	@Override
	public List<UserModel> getUsers() {
		return users;
	}

	@Override
	public int addUser(UserModel user) {
		user.setId(users.size() + 1);
		if (users.add(user)) {
			return 1;
		}
		return 0;
	}

	@Override
	public boolean deleteUser(int id) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getId() == id) {
				users.remove(i);
				return true;
			}
		}
		return false;
	}

	@Override
	public UserModel updateUser(UserModel updated) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getId() == updated.getId()) {
				users.add(i, updated);
				return updated;
			}
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
