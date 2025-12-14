package com.example.mymemories.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.mymemories.dto.ListResponseDTO;

@ControllerAdvice

public class GlobalExceptionHandler {

	 @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<ListResponseDTO<Object>> handleNotFound(
	            ResourceNotFoundException ex) {

	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ListResponseDTO<>(
	                        false,
	                        ex.getMessage(),
	                        List.of()
	                ));
	    }

	    @ExceptionHandler(UnauthorizedException.class)
	    public ResponseEntity<ListResponseDTO<Object>> handleUnauthorized(
	            UnauthorizedException ex) {

	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(new ListResponseDTO<>(
	                        false,
	                        ex.getMessage(),
	                        List.of()
	                ));
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ListResponseDTO<Object>> handleGeneric(Exception ex) {

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ListResponseDTO<>(
	                        false,
	                        "Something went wrong",
	                        List.of()
	                ));
	    }
	
	
}
