package com.gcep.model;

/**
 * Represents a simple object for accepting searches as JSON body
 * @author Gabriel Cepleanu
 * @version 0.2
 */
public class SearchModel {
	
	private String search;
	
	public SearchModel() {
		this.search = null;
	}

	public SearchModel(String search) {
		super();
		this.search = search;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	
	

}
