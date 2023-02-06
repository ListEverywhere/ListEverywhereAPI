package com.gcep.service;

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
	
	@Value("${client.registration.name}")
	private String regName;
	
	@Value("${spring.security.oauth2.client.registration.gcep.client-id}")
	private String clientId;
	
	private String currentToken = "";
	
	public ApiTokenManager(OAuth2AuthorizedClientManager clientMan) {
		this.clientMan = clientMan;
	}
	
	private String getAccessToken() {
		OAuth2AuthorizeRequest authorize = OAuth2AuthorizeRequest.withClientRegistrationId(regName).principal(clientId).build();
		
		OAuth2AuthorizedClient client = this.clientMan.authorize(authorize);
		
		OAuth2AccessToken token = client.getAccessToken();
		
		this.currentToken = token.getTokenValue();
		
		return this.currentToken;
	}
	
	public String getToken() {
		if (this.currentToken.equals("")) {
			return this.getAccessToken();
		}
		return currentToken;
	}
	
	

}
