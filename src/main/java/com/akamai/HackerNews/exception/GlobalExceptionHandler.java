/******************************************************************************

Copyright (c) 2023 Tal Aharon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

******************************************************************************/

package com.akamai.HackerNews.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/*****************************************************************************
 * @description: GlobalExceptionHandler controller advice class that handles 
 * exceptions globally for the application in the controller layer.
******************************************************************************/

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler
{
    /**************************************************************************
     * @description: Handles ValidationException and returns a bad request 
     * response with the exception message.
    **************************************************************************/
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException
    (ValidationException exception)
    {
        return (ResponseEntity.badRequest().body(exception.getMessage()));
    }

    /**************************************************************************
     * @description: Handles NewsPostNotFoundException and returns a not found 
     * response with the exception message.
    **************************************************************************/
    @ExceptionHandler(NewsPostNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException
    (NewsPostNotFoundException exception)
    {
        return (ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage()));
    }

    /**************************************************************************
    * @description: Handles MethodArgumentNotValidException and returns a bad 
    * request response with validation error messages.
    **************************************************************************/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors
    (MethodArgumentNotValidException exception)
    {
        List<String> errors = exception.getBindingResult().
        getFieldErrors().stream().map(this::buildErrorMessage).collect(Collectors.toList());
        String errorMessage = "Request failed: " + String.join(", ", errors);
        return (ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage));
    }

    /**************************************************************************
     * @description: Handles generic exceptions and returns an internal server 
     * error response with an error message.
    **************************************************************************/
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception exception)
    {
        return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An error occurred: " + exception.getMessage()));
    }

    /**************************************************************************
     * @description      : Private error message builder from a FieldError.
     * @param fieldError : The FieldError containing error information.
     * @return           : The formatted error message.
    **************************************************************************/
    private String buildErrorMessage(FieldError fieldError)
    {
        return (fieldError.getDefaultMessage() + " (" + fieldError.getField() + ")");
    }
}
