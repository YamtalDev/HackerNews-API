package com.akamai.MiniHackerNews.schema;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Entity
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
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

    @CreatedDate
    @Column(name = "creation_time", updatable = false)
    @PastOrPresent(message = "Creation time must be in the past or present")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_time")
    @PastOrPresent(message = "Updating time must be in the past or present")
    private LocalDateTime updatedAt;

    @Column(name = "votes")
    @Min(value = 0, message = "Votes must be a non-negative value")
    @Max(value = Integer.MAX_VALUE, message = "Maximum votes reached")
    private int votes;

    @PreUpdate
    public void preUpdate()
    {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist()
    {
        votes = 0;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}
