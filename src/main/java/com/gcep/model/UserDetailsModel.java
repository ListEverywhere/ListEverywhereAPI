package com.gcep.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserDetailsModel extends User {
	
	private static final long serialVersionUID = 1923527094429164656L;
	int user_id;

	public UserDetailsModel(String username, String password, List<GrantedAuthority> authorities, int user_id) {
		super(username, password, authorities);
		this.user_id = user_id;
	}
	
	public int getUserId() {
		return user_id;
	}
	
	

}
