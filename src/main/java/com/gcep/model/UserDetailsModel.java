package com.gcep.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserDetailsModel extends User {
	
	private static final long serialVersionUID = 1923527094429164656L;
	int id;

	public UserDetailsModel(String username, String password, List<GrantedAuthority> authorities, int id) {
		super(username, password, authorities);
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	

}
