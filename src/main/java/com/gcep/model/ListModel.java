package com.gcep.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ListModel {

	int list_id;
	String list_name;
	LocalDate creation_date;
	LocalDateTime last_modified;
	int user_id;
	List<ItemModel> list_items;
	
	public ListModel() {
		super();
	}

	public ListModel(int list_id, String list_name, LocalDate creation_date, LocalDateTime last_modified, int user_id,
			List<ItemModel> list_items) {
		super();
		this.list_id = list_id;
		this.list_name = list_name;
		this.creation_date = creation_date;
		this.last_modified = last_modified;
		this.user_id = user_id;
		this.list_items = list_items;
	}

	public int getListId() {
		return list_id;
	}

	public void setListId(int list_id) {
		this.list_id = list_id;
	}

	public String getListName() {
		return list_name;
	}

	public void setListName(String list_name) {
		this.list_name = list_name;
	}

	public LocalDate getCreationDate() {
		return creation_date;
	}

	public void setCreationDate(LocalDate creation_date) {
		this.creation_date = creation_date;
	}

	public LocalDateTime getLastModified() {
		return last_modified;
	}

	public void setLastModified(LocalDateTime last_modified) {
		this.last_modified = last_modified;
	}

	public int getUserId() {
		return user_id;
	}

	public void setUserId(int user_id) {
		this.user_id = user_id;
	}

	public List<ItemModel> getListItems() {
		return list_items;
	}

	public void setListItems(List<ItemModel> list_items) {
		this.list_items = list_items;
	}
	
	
	
}
