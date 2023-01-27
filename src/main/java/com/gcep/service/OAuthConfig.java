package com.gcep.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.UnAuthenticatedServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OAuthConfig {
	
	
	@Bean
	WebClient itemsClient(ReactiveClientRegistrationRepository clientReg) {
		
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
				new ServerOAuth2AuthorizedClientExchangeFilterFunction(
						clientReg,
						new UnAuthenticatedServerOAuth2AuthorizedClientRepository()
						);
		oauth.setDefaultClientRegistrationId("gcep");
		return WebClient.builder()
				.filter(oauth)
				.build();
				
	}

}
