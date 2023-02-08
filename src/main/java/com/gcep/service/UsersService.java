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

@Service
public class UsersService implements UserDetailsService {
	
	@Autowired
	UsersDataServiceInterface usersDataService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel user = usersDataService.getUserByUsername(username);
		
		if (user != null) {
			List<GrantedAuthority> access = new ArrayList<GrantedAuthority>();
			access.add(new SimpleGrantedAuthority("USER"));
			return new User(user.getUsername(), user.getPassword(), access);
		}
		else {
			throw new UsernameNotFoundException("User not found.");
		}
	}

}
