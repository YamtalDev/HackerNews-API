package com.akamai.MiniHackerNews.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/******************************************************************************
 * Custom exception for resource not found scenarios.
******************************************************************************/
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException
{
    private String fieldName;
    private Object fieldValue;
    private String resourceName;

    /**************************************************************************
     * New ResourceNotFoundException Ctor.
     * @param fieldName    The name of the field that caused the exception.
     * @param fieldValue   The value of the field that caused the exception.
     * @param resourceName The name of the resource that was not found.
    **************************************************************************/

    public ResourceNotFoundException(String fieldName, Object fieldValue, String resourceName)
    {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.resourceName = resourceName;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public Object getFieldValue()
    {
        return fieldValue;
    }

    public String getResourceName()
    {
        return resourceName;
    }
}
