package com.akamai.MiniHackerNews.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/******************************************************************************
 * @description : Custom exception class for handling data base validation errors 
 *                in the service layer.
******************************************************************************/
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ValidationException extends RuntimeException
{
    public ValidationException(String message)
    {
        super(message);
    }
}
