package com.akamai.MiniHackerNews.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
 * @description : 

******************************************************************************/
public class NewsPostRequestDTO
{
    @Column(name = "title", nullable = false)
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Post title must be between 1 and 255 characters")
    private String title;

    @Column(name = "user_name", nullable = false)
    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 20, message = "User name must be between 3 to 20 characters")
    private String userName;

    @NotBlank
    @Column(name = "link")
    @URL(message = "Invalid URL. Please provide a valid HTTP or HTTPS URL.")
    @Size(min = 10, max = 1024, message = "Link must be between 10 to 1024 characters long")
    private String link;

    public String getTitle()
    {
        return title;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getLink()
    {
        return link;
    }
}
