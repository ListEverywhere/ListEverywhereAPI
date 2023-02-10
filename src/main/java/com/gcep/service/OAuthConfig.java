package com.gcep.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestTemplate;

/**
 * This class holds the configuration for enabling the OAuth2 client and RestTemplate.
 * @author Gabriel Cepleanu
 * @version 0.1.1
 *
 */
@Configuration
public class OAuthConfig {
	
	
	/**
	 * Initializes the default RestTemplate
	 * @return Rest Template
	 */
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	/**
	 * Initializes the default OAuth2 client manager using client credentials.
	 * @param clientReg Client registration repository
	 * @param clientRep Client provider
	 * @return OAuth2 Client manager
	 */
	@Bean OAuth2AuthorizedClientManager clientMan(ClientRegistrationRepository clientReg,
			OAuth2AuthorizedClientRepository clientRep) {
		
		// Initializes the client provider using client credentials
		OAuth2AuthorizedClientProvider clientProvider =
				OAuth2AuthorizedClientProviderBuilder.builder()
				.clientCredentials()
				.build();
		
		// initializes the client manager using default properties
		var clientMan = new DefaultOAuth2AuthorizedClientManager(clientReg, clientRep);
		clientMan.setAuthorizedClientProvider(clientProvider);
		
		return clientMan;
	}

}
