package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleProductNotFoundException(ProductNotFoundException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage());
        logger.error("Product not found exception: {}", e.getMessage(), e);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage());
        logger.error("An error occurred: {}", e.getMessage(), e);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ErrorDetails> handleInvalidProductException(InvalidProductException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage());
        logger.error("An error occurred: {}", e.getMessage(), e);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
