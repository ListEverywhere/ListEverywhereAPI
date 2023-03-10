package com.gcep.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

/**
 * This class manages the OAuth2 tokens for the FatSecret Platform API integration.
 * @author Gabriel Cepleanu
 *
 */
@Component
public class ApiTokenManager {
	
	@Autowired
	private OAuth2AuthorizedClientManager clientMan;
	
	// Name for oauth2 client as defined in application.properties
	@Value("${client.registration.name}")
	private String regName;
	
	// Client ID for oauth2 service
	@Value("${spring.security.oauth2.client.registration.gcep.client-id}")
	private String clientId;
	
	private String currentToken = "";
	private Instant tokenExpiresAt = Instant.MAX;

	public ApiTokenManager(OAuth2AuthorizedClientManager clientMan) {
		this.clientMan = clientMan;
	}
	
	/**
	 * Creates a new access token from the FatSecret Platform API.
	 * @return Access token
	 */
	private String getAccessToken() {
		// initialize client with information supplied
		OAuth2AuthorizeRequest authorize = OAuth2AuthorizeRequest.withClientRegistrationId(regName).principal(clientId).build();
		
		// attempt authorization
		OAuth2AuthorizedClient client = this.clientMan.authorize(authorize);
		
		// get the token from the request
		OAuth2AccessToken token = client.getAccessToken();
		
		// get the token String value and set class variable
		this.tokenExpiresAt = token.getExpiresAt();
		this.currentToken = token.getTokenValue();
		
		// return current token
		return this.currentToken;
	}
	
	/**
	 * Returns the current access token for the FatSecret Platform API.
	 * If the current token is blank, a new token is generated.
	 * @return Access token
	 */
	public String getToken() {
		// Generate a new token if there is no current token or token has expired
		if (this.currentToken.equals("") || tokenExpiresAt.isBefore(Instant.now())) {
			return this.getAccessToken();
		}
		return currentToken;
	}
	
	

}
