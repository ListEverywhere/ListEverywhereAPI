package com.gcep.model;

/**
 * This class represents a Status containing a key and message.
 * This is used for sending success and error JSON responses in the REST APIs.
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public class StatusModel {

	/**
	 * The current status (error, success)
	 */
	private String status;
	/**
	 * Any status messages
	 */
	private String[] message;
	
	/**
	 * @param status The current status
	 * @param message A single status message
	 */
	public StatusModel(String status, String message) {
		this.status = status;
		this.message = new String[] {message};
	}
	
	/**
	 * 
	 * @param status The current status
	 * @param message Array of status messages
	 */
	public StatusModel(String status, String[] message) {
		this.status = status;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String[] getMessage() {
		return message;
	}

	public void setMessage(String[] message) {
		this.message = message;
	}
	
	
}
