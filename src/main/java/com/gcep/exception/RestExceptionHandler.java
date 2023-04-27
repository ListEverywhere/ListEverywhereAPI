package com.gcep.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.gcep.model.StatusModel;

/**
 * This class intercepts various controller and data service exceptions and reports a readable JSON error response
 * @author Gabriel Cepleanu
 * @version 0.1
 *
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

	@CrossOrigin
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		// get all validation errors and place them in a list
		List<String> err = ex.getBindingResult().getFieldErrors().stream().map(msg -> msg.getDefaultMessage()).collect(Collectors.toList());
		// create error StatusModel containing all field errors
		StatusModel retStatus = new StatusModel("error", err.toArray(new String[err.size()]));
		
		// return StatusModel JSON data
		return new ResponseEntity<>(retStatus, headers, status);
		
	}
	
	/**
	 * Handles any database exceptions and returns a simple message
	 * @param ex The exception that was caught
	 * @return JSON StatusModel object with the exception message
	 */
	@CrossOrigin
	@ExceptionHandler(DatabaseErrorException.class)
	protected ResponseEntity<Object> handleDatabaseExeption(Exception ex) {
		StatusModel retStatus = new StatusModel("error", ex.getMessage());
		log.error("ListEverywhere API ERROR: " + ex.getMessage());
		return new ResponseEntity<>(retStatus, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
