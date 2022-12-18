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
	
	public DatabaseErrorException(String msg) {
		super(msg);
	}

}
