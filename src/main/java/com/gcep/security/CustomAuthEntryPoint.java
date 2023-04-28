package com.gcep.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcep.model.StatusModel;

/**
 * This class handles errors with JWT authentication and displays a JSON response page if an error occurs (such as not being authenticated)
 * @author Gabriel Cepleanu
 * @verison 0.2
 */
@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), new StatusModel("error", authException.getMessage()));

	}

}
