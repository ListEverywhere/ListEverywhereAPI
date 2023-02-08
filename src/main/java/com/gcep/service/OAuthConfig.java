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

@Configuration
public class OAuthConfig {
	
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean OAuth2AuthorizedClientManager clientMan(ClientRegistrationRepository clientReg,
			OAuth2AuthorizedClientRepository clientRep) {
		OAuth2AuthorizedClientProvider clientProvider =
				OAuth2AuthorizedClientProviderBuilder.builder()
				.clientCredentials()
				.build();
		
		var clientMan = new DefaultOAuth2AuthorizedClientManager(clientReg, clientRep);
		clientMan.setAuthorizedClientProvider(clientProvider);
		
		return clientMan;
	}

}
