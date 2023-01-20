package com.gcep.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.gcep.service.UsersService;

@Configuration
public class AppSecurityConfig {
	
	@Autowired
	private UsersService usersService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.httpBasic().and().authorizeRequests()
		.antMatchers("/users/**").authenticated()
		.and()
		.authorizeRequests()
		.antMatchers("/test").permitAll()
		.anyRequest().authenticated()
		;
		
		return http.build();
	}
	
	@Bean
	public static PasswordEncoder passwordEncode() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		authProvider.setUserDetailsService(usersService);
		authProvider.setPasswordEncoder(passwordEncode());
		
		return authProvider;
		
	}
	
}
