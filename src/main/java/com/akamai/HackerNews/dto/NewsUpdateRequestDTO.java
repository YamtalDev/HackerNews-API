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

package com.akamai.HackerNews.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

/******************************************************************************
 * @dto : This DTO entity is used for client PATCH requests.
******************************************************************************/
public class NewsUpdateRequestDTO
{
    @NotBlank(message = "Post is required")
    @Column(name = "post", updatable = true)
    @Size(min = 1, max = 1024, message = "Post must be between 1 and 1024 characters")
    private String post;

    @NotBlank(message = "Link url is required.")
    @Column(name = "link", nullable = false, updatable = true)
    @URL(message = "Invalid URL. Please provide a valid HTTP or HTTPS URL.")
    @Size(min = 10, max = 1024, message = "Link must be between 10 to 1024 characters long")
    private String link;

    /**************************************************************************
     * @Getters : Defined for ModelMapper.
    **************************************************************************/
    public String getPost(){return (post);}
    public String getLink(){return (link);}
    public void setPost(String post){this.post = post;}
    public void setLink(String link){this.link = link;}
}
