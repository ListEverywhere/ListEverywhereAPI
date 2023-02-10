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

/**
 * This class contains the methods for receiving Food Item data from the FatSecret Platform API.
 * @author Gabriel Cepleanu
 * @version 0.1.1
 *
 */
@Service
public class ItemsService {
	public static final String AUTHORIZE = "Authorization";
	
	// base URL for APi requests
	private String resourceUrl = "https://platform.fatsecret.com/rest/server.api";
	
	@Autowired
	private ApiTokenManager apiTokenManager;
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Returns a single Food Item with the given food ID number.
	 * If the food item ID is invalid, item name is set to "Unknown"
	 * @param food_id ID number of the food item
	 * @return FoodItemModel object
	 */
	public FoodItemModel getItem(int food_id) {
		// gets the current access token
		String token = apiTokenManager.getToken();
		
		// builds request URL and adds query parameters
		String url = UriComponentsBuilder.fromHttpUrl(resourceUrl)
				.queryParam("method", "food.get.v2")
				.queryParam("food_id", food_id)
				.queryParam("format", "json")
				.encode()
				.toUriString();
		
		// adds authorization header containing access token
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZE, "Bearer " + token);
		HttpEntity<Void> req = new HttpEntity<>(headers);
		
		// initializes object containing the return type
		ParameterizedTypeReference<Map<String, FoodItemModel>> respType = new ParameterizedTypeReference<Map<String, FoodItemModel>>() {};
		
		// sends HTTP request
		var response = restTemplate.exchange(url, HttpMethod.POST, req, respType);
		
		// get the FoodItemModel from the JSON data
		FoodItemModel retval = response.getBody().get("food");
		
		// if the food item is null, set name to "Unknown"
		if (retval == null) {
			return new FoodItemModel(food_id, "Unknown");
		}
		return retval;
	}
	
	/**
	 * Returns a list of items with the matching search term.
	 * Search includes items that contain the specified string.
	 * @param search_term Search term
	 * @return List of Food Item objects
	 * @throws IOException
	 */
	public List<FoodItemModel> searchItems(String search_term) throws IOException {
		// get the access token
		String tkn = apiTokenManager.getToken();
		
		// builds request URL and adds query parameters
		String url = UriComponentsBuilder.fromHttpUrl(resourceUrl)
				.queryParam("method", "foods.search")
				.queryParam("search_expression", search_term)
				.queryParam("format", "json")
				.queryParam("max_results", 10)
				.encode()
				.toUriString();
		
		// adds authorization header containing access token
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZE,  "Bearer " + tkn);
		HttpEntity<Void> req = new HttpEntity<>(headers);
		
		// sends HTTP request
		var response = restTemplate.exchange(url, HttpMethod.POST, req, String.class);
		
		// creates the object mapper instance
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
