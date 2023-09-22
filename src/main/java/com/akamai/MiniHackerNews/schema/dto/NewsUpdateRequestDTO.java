package com.akamai.MiniHackerNews.schema.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class NewsUpdateRequestDTO
{
    @NotBlank(message = "Post is required")
    @Column(name = "post", nullable = false, updatable = true)
    @Size(min = 1, max = 1024, message = "Post must be between 1 and 1024 characters")
    private String post;

    @NotBlank(message = "Url is required.")
    @Column(name = "link", nullable = false, updatable = true)
    @URL(message = "Invalid URL. Please provide a valid HTTP or HTTPS URL.")
    @Size(min = 10, max = 1024, message = "Link must be between 10 to 1024 characters long")
    private String link;

    public String getPost()
    {
        return (post);
    }

    public String getLink()
    {
        return (link);
    }
}
