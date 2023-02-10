package com.gcep.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This class configures the filter for all requests using JWT authentication.
 * @author Gabriel Cepleanu
 * @version 0.1.1
 */
@Component
public class TokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenUtility tokenUtility;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// if authentication header is missing, skip token check
		// moves to next filter which will return 403
		if (!hasAuthenticationHeader(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		// get the token from the request
		String tkn = getToken(request);
		
		// if token is not valid, move to next filter which returns 403
		if (!tokenUtility.validateToken(tkn)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		// token is authenticated, get user details and move to next filter
		setContext(tkn, request);
		filterChain.doFilter(request, response);
		
		
	}
	
	/**
	 * Checks an HTTPServletRequest for Authorization header using Bearer token.
	 * @param req HTTP Request
	 * @return True if the header exists
	 */
	private boolean hasAuthenticationHeader(HttpServletRequest req) {
		// get the Authorization header
		String header = req.getHeader("Authorization");
		// check that header is not empty and contains Bearer token
		return header != null && !header.isEmpty() && header.startsWith("Bearer");
	}
	
	/**
	 * Returns the token string from an HTTP request with Authorization header
	 * @param req HTTP request
	 * @return JWT Token string
	 */
	private String getToken(HttpServletRequest req) {
		String header = req.getHeader("Authorization");
		return header.split(" ")[1].trim();
	}
	
	/**
	 * Updates the context with an authenticated user. Method will only be called after token is authenticated.
	 * @param tkn Valid JWT Token
	 * @param req HTTP Request
	 */
	private void setContext(String tkn, HttpServletRequest req) {
		// get the User using the information from token subject
		UserDetails user = getUserDetails(tkn);
		
		// initializes Username and Password authentication
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, null);
		
		// initializes Authentication details instance
		auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
		
		// sets the authentication with the current user
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	/**
	 * Returns a User object from a valid token
	 * @param tkn JWT token string
	 * @return UserDetails object
	 */
	private UserDetails getUserDetails(String tkn) {
		// gets the subject from the token (username and password)
		String[] subject = tokenUtility.getSubject(tkn).split(",");
		
		// Sets the roles of the user (currently just user)
		List<GrantedAuthority> access = new ArrayList<GrantedAuthority>();
		access.add(new SimpleGrantedAuthority("USER"));
		
		// Creates the user object using the information from the token
		User user = new User(subject[0], subject[1], access);
		return user;
		
	}

}
