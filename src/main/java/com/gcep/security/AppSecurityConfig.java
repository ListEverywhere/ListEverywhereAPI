package com.gcep.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gcep.service.UsersService;

/**
 * This class holds the configuration for Spring Security, including Http Security, PasswordEncoder, and AuthenticationManager
 * @author Gabriel Cepleanu
 * @version 0.1.1
 */
@Configuration
public class AppSecurityConfig {
	
	@Autowired
	private UsersService usersService;
	@Autowired
	private TokenFilter tokenFilter;
	@Autowired
	private CustomAuthEntryPoint authEntryPoint;
	
	
	/**
	 * Configures HTTP security for the application endpoints.
	 * Also initializes OAuth2 client and JWT Token filter classes.
	 * @param http Http Security
	 * @return HTTP security builder
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		// initializes the OAuth2 client for third party API integration
		http.oauth2Client();
		
		// configures the application endpoints
		// all endpoints require valid JWT token except for login and register
		http.cors().and().csrf().disable()
		.authorizeRequests()
		.antMatchers("/lists/**").authenticated()
		.and()
		.authorizeRequests()
		.antMatchers("/recipes/**").authenticated()
		.and()
		.authorizeRequests()
		.antMatchers("/items/**").authenticated()
		.and()
		.authorizeRequests()
		.antMatchers("/users/login/").permitAll()
		.and()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/users/").permitAll()
		.and()
		.authorizeRequests()
		.antMatchers("/users/**").authenticated()
		.anyRequest().authenticated()
		.and()
		.exceptionHandling().authenticationEntryPoint(authEntryPoint)
		;
		
		// configures the JWT token filter
		http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
		
		// returns HTTP security builder
		return http.build();
	}
	
	/**
	 * Initializes the PasswordEncoder for database passwords using BCrypt
	 * @return
	 */
	@Bean
	public static PasswordEncoder passwordEncode() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * Configures the UserDetailsService for authentication using encrypted passwords.
	 * @return DAO Authentication provider
	 */
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		// set the user service and password encoder
		authProvider.setUserDetailsService(usersService);
		authProvider.setPasswordEncoder(passwordEncode());
		
		return authProvider;
		
	}
	
	/**
	 * Configures the authentication manager to process authentication requests
	 * @param authConfig
	 * @return Authentication manager
	 * @throws Exception
	 */
	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
}
