package com.akamai.MiniHackerNews.schema;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.URL;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
 * @description:  
******************************************************************************/

@Data
@Entity
@JsonInclude
@SuperBuilder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Posts", indexes = @Index(name = "rank_index", columnList = "rank DESC"))
/******************************************************************************
 * News post model represents a post for the Mini Hacker News system.
 * Each news post contains a title, text, creation time, updated time, and vote counts.
******************************************************************************/
public class NewsPostEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Max(value = Long.MAX_VALUE, message = "Maximum posts capacity reached.")
    private Long post_id;

    @Column(name = "post", nullable = false)
    @NotBlank(message = "Post is required")
    @Size(min = 1, max = 1024, message = "Post must be between 1 and 1024 characters")
    private String post;

    @Column(name = "user_name", nullable = false)
    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 20, message = "User name must be between 3 to 20 characters")
    private String userName;

    @Column(name = "link")
    @NotBlank(message = "Url is required.")
    @URL(message = "Invalid URL. Please provide a valid HTTP or HTTPS URL.")
    @Size(min = 10, max = 1024, message = "Link must be between 10 to 1024 characters long")
    private String link;

    @CreatedDate
    @Column(name = "creation_time", updatable = false)
    @PastOrPresent(message = "Creation time must be in the past or present")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_time", updatable = true)
    @PastOrPresent(message = "Updating time must be in the past or present")
    private LocalDateTime updatedAt;

    @Min(value = 0, message = "post time must be a non-negative value")
    @Max(value = Long.MAX_VALUE, message = "Maximum time reached")
    @Column(name = "post_time", updatable = true)
    private long postTime;

    @Column(name = "rank")
    @Min(value = 0, message = "Rank must be a non-negative value")
    @Max(value = Long.MAX_VALUE, message = "Maximum rank reached")
    private double rank;

    @Column(name = "votes")
    @Min(value = 0, message = "Votes must be a non-negative value")
    @Max(value = Integer.MAX_VALUE, message = "Maximum votes reached")
    private int votes;

    @PreUpdate
    public void preUpdate()
    {
        updatedAt = LocalDateTime.now();
        postTime = ChronoUnit.SECONDS.between(createdAt, updatedAt) / 36000;
        rank = votes / Math.pow((postTime + 2), 1.8);
    }

    @PrePersist
    public void prePersist()
    {
        votes = 0;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public Long getPostId()
    {
        return (post_id);
    }
}