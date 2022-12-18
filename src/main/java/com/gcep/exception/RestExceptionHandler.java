package com.gcep.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.gcep.model.StatusModel;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<String> err = ex.getBindingResult().getFieldErrors().stream().map(msg -> msg.getDefaultMessage()).collect(Collectors.toList());
		StatusModel retStatus = new StatusModel("error", err.toArray(new String[err.size()]));
		
		return new ResponseEntity<>(retStatus, headers, status);
		
	}
}
