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

@Component
public class TokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenUtility tokenUtility;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		if (!hasAuthenticationHeader(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String tkn = getToken(request);
		
		if (!tokenUtility.validateToken(tkn)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		setContext(tkn, request);
		filterChain.doFilter(request, response);
		
		
	}
	
	private boolean hasAuthenticationHeader(HttpServletRequest req) {
		String header = req.getHeader("Authorization");
		return header != null && !header.isEmpty() && header.startsWith("Bearer");
	}
	
	private String getToken(HttpServletRequest req) {
		String header = req.getHeader("Authorization");
		return header.split(" ")[1].trim();
	}
	
	private void setContext(String tkn, HttpServletRequest req) {
		UserDetails user = getUserDetails(tkn);
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, null);
		
		auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
		
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	private UserDetails getUserDetails(String tkn) {
		String[] subject = tokenUtility.getSubject(tkn).split(",");
		
		List<GrantedAuthority> access = new ArrayList<GrantedAuthority>();
		access.add(new SimpleGrantedAuthority("USER"));
		
		User user = new User(subject[0], subject[1], access);
		return user;
		
	}

}
