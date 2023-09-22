package com.akamai.MiniHackerNews.exception;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException
    (ValidationException exception)
    {
        return (ResponseEntity.badRequest().body(exception.getMessage()));
    }

    @ExceptionHandler(NewsPostNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException
    (NewsPostNotFoundException exception)
    {
        return (ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors
    (MethodArgumentNotValidException exception)
    {
        List<String> errors = exception.getBindingResult().
        getFieldErrors().stream().map(this::buildErrorMessage).collect(Collectors.toList());
        String errorMessage = "Request failed: " + String.join(", ", errors);
        return (ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage));
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception exception)
    {
        return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An error occurred: " + exception.getMessage()));
    }

    private String buildErrorMessage(FieldError fieldError)
    {
        return (fieldError.getDefaultMessage() + " (" + fieldError.getField() + ")");
    }
}
