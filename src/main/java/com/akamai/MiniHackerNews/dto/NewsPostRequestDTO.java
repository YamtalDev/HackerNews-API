package com.akamai.MiniHackerNews.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

/******************************************************************************
 * @dto : This DTO entity is used for client PUT and POST requests.
******************************************************************************/
public class NewsPostRequestDTO
{
    @NotBlank(message = "User name is required")
    @Column(name = "posted_by", updatable = true)
    @Size(min = 1, max = 20, message = "User name must be between 3 to 20 characters")
    private String postedBy;

    @NotBlank(message = "Post is required")
    @Column(name = "post", updatable = true)
    @Size(min = 1, max = 1024, message = "Post must be between 1 and 1024 characters")
    private String post;

    @NotBlank(message = "Link url is required.")
    @Column(name = "link", updatable = true)
    @URL(message = "Invalid URL. Please provide a valid HTTP or HTTPS URL.")
    @Size(min = 10, max = 1024, message = "Link must be between 10 to 1024 characters long")
    private String link;

    /**************************************************************************
     * @Getters : Defined for the ModelMapper.
    **************************************************************************/
    public String getPost(){return (post);}
    public String getLink(){return (link);}
    public String getPostedBy(){return (postedBy);}
}
