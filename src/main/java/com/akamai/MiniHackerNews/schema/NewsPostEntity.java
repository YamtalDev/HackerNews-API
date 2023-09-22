package com.akamai.MiniHackerNews.schema;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;

/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
 * @description:  
******************************************************************************/

@Data
@Entity
@DynamicUpdate
@NoArgsConstructor
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

    @NotBlank(message = "Post is required")
    @Column(name = "post", nullable = false, updatable = true)
    @Size(min = 1, max = 1024, message = "Post must be between 1 and 1024 characters")
    private String post;

    @NotBlank(message = "User name is required")
    @Column(name = "posted_by", nullable = false, updatable = false)
    @Size(min = 3, max = 20, message = "User name must be between 3 to 20 characters")
    private String posted_by;

    @NotBlank(message = "Url is required.")
    @Column(name = "link", nullable = false, updatable = true)
    @URL(message = "Invalid URL. Please provide a valid HTTP or HTTPS URL.")
    @Size(min = 10, max = 1024, message = "Link must be between 10 to 1024 characters long")
    private String link;

    @CreatedDate
    @Column(name = "creation_time", updatable = false)
    @PastOrPresent(message = "Creation time must be in the past or present")
    private LocalDateTime created_at;

    @Column(name = "rank")
    @Min(value = 0, message = "Rank must be a non-negative value")
    @Max(value = Long.MAX_VALUE, message = "Maximum rank reached")
    private double rank;

    @Column(name = "votes")
    @Min(value = 0, message = "Votes must be a non-negative value")
    @Max(value = Integer.MAX_VALUE, message = "Maximum votes reached")
    private int votes;

    public int getVotes(){return (votes);}
    public String getPost(){return (post);}
    public Long getPostId(){return (post_id);}
    public String getPostedBy(){return (posted_by);}

    public void setVotes(int vote){this.votes += vote;}
    public void setPost(String post){this.post = post;}
    public void setLink(String link){this.link = link;}

    @PreUpdate
    public void updateRank()
    {
        long postTime = ChronoUnit.HOURS.between(created_at, LocalDateTime.now());
        this.rank = votes / Math.pow((postTime + 2), 1.8);
    }

    @PrePersist
    public void prePersist()
    {
        created_at = LocalDateTime.now();
    }
}