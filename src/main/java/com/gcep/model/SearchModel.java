package com.gcep.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Represents a simple object for accepting searches as JSON body
 * @author Gabriel Cepleanu
 * @version 1.1.0
 */
public class SearchModel {
	
	@NotNull
	@Size(min=3, max=50, message="Search term must be between 3 to 50 characters.")
	private String search;
	private String search_type = "contains";
	private int page_number = 0;
	
	public SearchModel() {
		this.search = null;
	}

	public SearchModel(String search) {
		super();
		this.search = search;
		this.search_type = "contains";
	}
	
	public SearchModel(String search, String search_type, int page_number) {
		super();
		this.search = search;
		this.search_type = search_type;
		this.page_number = page_number;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSearchType() {
		return search_type;
	}

	public void setSearchType(String search_type) {
		this.search_type = search_type;
	}
	
	public int getPageNumber() {
		return page_number;
	}
	
	public void setPageNumber(int page_number) {
		this.page_number = page_number;
	}
	
	
	
	

}
