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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/******************************************************************************
 * @description: Custom exception class for handling resource not found errors 
 * in the service layer.
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
    * Getters for exception handler.
    **************************************************************************/
    public String getFieldName(){return (fieldName);}
    public Object getFieldValue(){return (fieldValue);}
    public String getResourceName(){return (resourceName);}
}
