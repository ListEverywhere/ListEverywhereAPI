package com.gcep.exception;

/**
 * This exception is thrown if an error occurs in a data service, such as a failed SQL query.
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
public class DatabaseErrorException extends RuntimeException {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 4913341954771779424L;
	
	/**
	 * Empty constructor. Preloads a generic error message for a database error.
	 */
	public DatabaseErrorException() {
		super("There was an error accessing the database.");
	}
	
	/**
	 * Custom message constructor. Allows for a custom error to be displayed in the exception.
	 * @param msg The message for the exception.
	 */
	public DatabaseErrorException(String msg) {
		super(msg);
	}

}
