package com.gcep.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.gcep.model.UserDetailsModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * This class contains functions for generating and validating JWT tokens used for authentication.
 * @author Gabriel Cepleanu
 * @version 0.1.1
 *
 */
@Component
public class TokenUtility {
	
	// duration is 72 hours
	private static final long EXPIRES = 259200000;
	
	// Secret referenced from application.properties
	@Value("${app.listeverywhere.secret}")
	private String secret;
	
	/**
	 * Generates a JWT token using a user's username and password
	 * @param user User from UserDetailsService
	 * @return JWT token string
	 */
	public String generateToken(UserDetailsModel user) {
		
		// create key using the secret
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		
		// return JWT token string using Jwts builder
		// subject contains username and password
		// expiration uses date constant
		// sign using the secret
		return Jwts.builder().setSubject(String.format("%s,%s,%d", user.getUsername(), user.getPassword(), user.getUserId()))
				.setIssuer("gcep")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRES))
				.signWith(key)
				.compact();
				
	}
	
	/**
	 * Validates a JWT token string and returns the status
	 * @param tkn JWT token string
	 * @return True if valid token
	 */
	public boolean validateToken(String tkn) {
		
		// create key using secret
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		
		try {
			// uses Jwts parseClaimsJws method to verify key
			// if no exceptions are thrown, key is valid
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tkn);
			return true;
		} catch (Exception e) {
			// error trying to read JWT
			System.out.println(e.getMessage());
		}
		
		return false;
	}
	
	/**
	 * Returns the subject of the JWT token (username and password)
	 * @param tkn JWT token string
	 * @return Token subject
	 */
	public String getSubject(String tkn) {
		// create key using secret
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		
		// decodes the key and gets the body
		Claims c = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tkn).getBody();
		
		// returns the Subject as String
		return c.getSubject();
	}

}
