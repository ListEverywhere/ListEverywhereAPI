package com.gcep.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gcep.data.UsersDataServiceInterface;
import com.gcep.model.UserModel;

/**
 * This class contains the methods for receiving a UserDetails object from a database user entry.
 * @author Gabriel Cepleanu
 * @version 0.1.1
 */
@Service
public class UsersService implements UserDetailsService {
	
	@Autowired
	UsersDataServiceInterface usersDataService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// get the user from the database
		UserModel user = usersDataService.getUserByUsername(username);
		
		if (user != null) {
			// set the roles for the user
			// only role available currently is "USER"
			List<GrantedAuthority> access = new ArrayList<GrantedAuthority>();
			access.add(new SimpleGrantedAuthority("USER"));
			return new User(user.getUsername(), user.getPassword(), access);
		}
		else {
			throw new UsernameNotFoundException("User not found.");
		}
	}

}
