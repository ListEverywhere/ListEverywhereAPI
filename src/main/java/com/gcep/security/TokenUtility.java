package com.gcep.security;

import java.util.Date;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtility {
	
	// duration is 72 hours
	private static final long EXPIRES = 259200000;
	
	// make this read from app.properties instead of hard-coded
	private String secret = "testingTestingTESTING1234567890testingTestingTESTING1234567890";
	
	public String generateToken(User user) {
		
		// update this function to not use deprecated function
		return Jwts.builder().setSubject(String.format("%s,%s", user.getUsername(), user.getPassword()))
				.setIssuer("gcep")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRES))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

}
