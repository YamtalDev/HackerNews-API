package com.akamai.MiniHackerNews.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

/******************************************************************************
 * Represents a news post in the Mini Hacker News system.
 * Each news post contains a title, text, creation time, updated time, and vote counts.
******************************************************************************/

@Data
@Entity
@Table(name = "News-Post")
public class NewsPost
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "Title text is required")
    @Size(min = 1, max = 255, message = "Post text must be between 1 and 255 characters")
    private String title;

    @Column(name = "post", nullable = false)
    @NotBlank(message = "Post text is required")
    @Size(min = 1, max = 8096, message = "Post text must be between 1 and 8096 characters")
    private String text;

    @Column(name = "updated_time")
    @Past(message = "Updated time must be in the past")
    private LocalDateTime updatedTime;

    @Column(name = "creation_time")
    @PastOrPresent(message = "Creation time must be in the past or present")
    private LocalDateTime creationTime;

    @Column(name = "down_votes")
    @Min(value = 0, message = "Down votes cannot be negative")
    @Max(value = 10000, message = "Down votes cannot exceed 10000")
    private Long downVotes;

    @Column(name = "up_votes")
    @Min(value = 0, message = "Up votes cannot be negative")
    @Max(value = 10000, message = "Up votes cannot exceed 10000")
    private Long upVotes;
}
