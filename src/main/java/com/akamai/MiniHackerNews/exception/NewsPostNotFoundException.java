package com.akamai.MiniHackerNews.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/******************************************************************************
 * @description : Custom exception class for handling resource not found errors 
 *                in the service layer.
******************************************************************************/
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NewsPostNotFoundException extends RuntimeException
{
    private String fieldName;
    private Object fieldValue;
    private String resourceName;

    /**************************************************************************
     * @description        : ResourceNotFoundException Ctor. 
     * @param fieldName    : The name of the field that caused the exception.
     * @param fieldValue   : The value of the field that caused the exception.
     * @param resourceName : The name of the resource that was not found.
    **************************************************************************/
    public NewsPostNotFoundException(String fieldName, Object fieldValue, String resourceName)
    {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.resourceName = resourceName;
    }

    /**************************************************************************
    * Getters for the exception handler.
    **************************************************************************/
    public String getFieldName(){return (fieldName);}
    public Object getFieldValue(){return (fieldValue);}
    public String getResourceName(){return (resourceName);}
}
