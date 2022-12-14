package com.gcep.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
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

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<String> err = ex.getBindingResult().getFieldErrors().stream().map(msg -> msg.getDefaultMessage()).collect(Collectors.toList());
		StatusModel retStatus = new StatusModel("error", err.toArray(new String[err.size()]));
		
		return new ResponseEntity<>(retStatus, headers, status);
		
	}
	
	/**
	 * Handles any database exceptions and returns a simple message
	 * @param ex The exception that was caught
	 * @return JSON StatusModel object with the exception message
	 */
	@ExceptionHandler(DatabaseErrorException.class)
	protected ResponseEntity<Object> handleDatabaseExeption(Exception ex) {
		StatusModel retStatus = new StatusModel("error", ex.getMessage());
		return new ResponseEntity<>(retStatus, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
