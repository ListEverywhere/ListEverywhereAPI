package com.gcep.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Represents a simple object for accepting searches as JSON body
 * @author Gabriel Cepleanu
 * @version 0.2
 */
public class SearchModel {
	
	@NotNull
	@Size(min=3, max=50, message="Search term must be between 3 to 50 characters.")
	private String search;
	private String search_type = "contains";
	
	public SearchModel() {
		this.search = null;
	}

	public SearchModel(String search) {
		super();
		this.search = search;
		this.search_type = "contains";
	}
	
	public SearchModel(String search, String search_type) {
		super();
		this.search = search;
		this.search_type = search_type;
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
	
	
	
	

}
