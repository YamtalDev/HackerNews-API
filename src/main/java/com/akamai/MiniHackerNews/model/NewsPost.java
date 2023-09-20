package com.akamai.MiniHackerNews.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

@Data
@Entity
@Table(name = "News-Post")
/******************************************************************************
 * News post model represents a post for the Mini Hacker News system.
 * Each news post contains a title, text, creation time, updated time, and vote counts.
******************************************************************************/
public class NewsPost
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Max(value = Long.MAX_VALUE, message = "Maximum capacity of posts")
    private long post_id;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Post title must be between 1 and 255 characters")
    private String title;

    @Column(name = "user_name", nullable = false)
    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 20, message = "User name must be between 3 to 20 characters")
    private String userName;

    @Column(name = "creation_time")
    @PastOrPresent(message = "Creation time must be in the past or present")
    private LocalDateTime creationTime;

    @Column(name = "votes")
    @Min(value = 0, message = "Votes must be a non-negative value")
    @Max(value = Integer.MAX_VALUE, message = "Maximum votes reached")
    private int votes;

    @Column(name = "link")
    @URL(message = "Invalid URL. Please provide a valid HTTP or HTTPS URL.")
    @Size(min = 10, max = 1024, message = "Link must be between 10 to 1024 characters long")
    private String link;
}
