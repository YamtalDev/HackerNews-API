package com.akamai.MiniHackerNews.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException
    (ValidationException exception)
    {
        return (ResponseEntity.badRequest().body(exception.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException
    (ResourceNotFoundException exception)
    {
        return (ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage()));
    }
}
