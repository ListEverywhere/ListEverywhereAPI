package com.gcep.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gcep.model.FoodItemModel;

@Service
public class ItemsService {
	public static final String AUTHORIZE = "Authorization";
	
	private String resourceUrl = "https://platform.fatsecret.com/rest/server.api";
	
	@Autowired
	private ApiTokenManager apiTokenManager;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public FoodItemModel getItem(int food_id) {
		String token = apiTokenManager.getToken();
		
		String url = UriComponentsBuilder.fromHttpUrl(resourceUrl)
				.queryParam("method", "food.get.v2")
				.queryParam("food_id", food_id)
				.queryParam("format", "json")
				.encode()
				.toUriString();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZE, "Bearer " + token);
		HttpEntity<Void> req = new HttpEntity<>(headers);
		
		ParameterizedTypeReference<Map<String, FoodItemModel>> respType = new ParameterizedTypeReference<Map<String, FoodItemModel>>() {};
		
		var response = restTemplate.exchange(url, HttpMethod.POST, req, respType);
		
		FoodItemModel retval = response.getBody().get("food");
		
		if (retval == null) {
			return new FoodItemModel(food_id, "Unknown");
		}
		return retval;
	}
	
	public List<FoodItemModel> searchItems(String search_term) throws IOException {
		String tkn = apiTokenManager.getToken();
		
		String url = UriComponentsBuilder.fromHttpUrl(resourceUrl)
				.queryParam("method", "foods.search")
				.queryParam("search_expression", search_term)
				.queryParam("format", "json")
				.queryParam("max_results", 10)
				.encode()
				.toUriString();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZE,  "Bearer " + tkn);
		HttpEntity<Void> req = new HttpEntity<>(headers);
				
		var response = restTemplate.exchange(url, HttpMethod.POST, req, String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		
		// gets the list of items that are nested inside two keys
		JsonNode food = mapper.readTree(response.getBody()).get("foods").get("food");
		// create the reader that will parse JsonNode into objects
		ObjectReader reader = mapper.readerFor(new TypeReference<List<FoodItemModel>>() {});
		// convert JsonNode into a list of FoodItemModel objects
		List<FoodItemModel> items = reader.readValue(food);
		
		return items;
	}

}
