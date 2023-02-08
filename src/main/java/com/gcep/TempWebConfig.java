package com.gcep;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Temporary class for configuring spring security, merge with user-auth web config.
 * @author gabriel
 *
 */
@Configuration
public class TempWebConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.oauth2Client();
		
		http.csrf().disable().authorizeRequests().anyRequest().permitAll();
		
		return http.build();
	}
	

}
