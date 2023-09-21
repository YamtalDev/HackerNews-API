package com.akamai.MiniHackerNews.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewsPostResponseDTO
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Max(value = Long.MAX_VALUE, message = "Maximum capacity of posts")
    private Long post_id;

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

    @Column(name = "post_time", updatable = true)
    private String postTime;

    @Column(name = "votes")
    @Min(value = 0, message = "Votes must be a non-negative value")
    @Max(value = Integer.MAX_VALUE, message = "Maximum votes reached")
    private int votes;
}
