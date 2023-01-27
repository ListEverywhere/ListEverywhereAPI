package com.gcep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ItemsService {
	
	@Autowired
	private WebClient itemsClient;
	
	public void test() {
		var retval = itemsClient.post()
		.uri("https://platform.fatsecret.com/rest/server.api?method=food.get.v2&food_id=38821&format=json")
		.retrieve()
		.bodyToMono(String.class)
		.map(string -> "Data: " + string)
		.block();
		
		System.out.println(retval);
	}

}
