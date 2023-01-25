package com.gcep.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenUtility {
	
	// duration is 72 hours
	private static final long EXPIRES = 259200000;
	
	@Value("${app.listeverywhere.secret}")
	private String secret;
	
	public String generateToken(User user) {
		
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		
		return Jwts.builder().setSubject(String.format("%s,%s", user.getUsername(), user.getPassword()))
				.setIssuer("gcep")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRES))
				.signWith(key)
				.compact();
				
	}
	
	public boolean validateToken(String tkn) {
		
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tkn);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return false;
	}
	
	public String getSubject(String tkn) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		
		Claims c = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tkn).getBody();
		
		return c.getSubject();
	}

}
