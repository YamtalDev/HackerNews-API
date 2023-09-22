/******************************************************************************
MIT License

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

@Data
@Entity
@DynamicUpdate
@NoArgsConstructor
@Table(name = "Posts", indexes = @Index(name = "rank_index", columnList = "rank DESC"))
/******************************************************************************
 * News post model represents a post for the Mini Hacker News system.
 * Each news post contains a title, text, creation time, updated time, and vote counts.
******************************************************************************/
public class NewsPostSchema
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Max(value = Long.MAX_VALUE, message = "Maximum posts capacity reached.")
    private Long postId;

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

    @CreatedDate
    @Column(name = "creation_time", updatable = true)
    private LocalDateTime time;

    @Column(name = "rank", updatable = true)
    @Min(value = 0, message = "Rank must be a non-negative value")
    @Max(value = Long.MAX_VALUE, message = "Maximum rank reached")
    private double rank;

    @Column(name = "votes", updatable = true)
    @Min(value = 0, message = "Votes must be a non-negative value")
    @Max(value = Integer.MAX_VALUE, message = "Maximum votes reached")
    private int votes;

    public int getVotes()
    {
        return (votes);
    }

    public String getPost()
    {
        return (post);
    }

    public Long getPostId()
    {
        return (postId);
    }

    public String getPostedBy()
    {
        return (postedBy);
    }

    public LocalDateTime getTime()
    {
        return (time);
    }

    public void upVote()
    {
        ++this.votes;
    }

    public void downVote()
    {
        --this.votes;
    }

    public void setPost(String post)
    {
        this.post = post;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public void setPostedBy(String posted_by)
    {
        this.postedBy = posted_by;
    }

    @PrePersist
    public void setTime()
    {
        this.time = LocalDateTime.now();
    }

    @PreUpdate
    public void updateRank()
    {
        long postTime = ChronoUnit.HOURS.between(time, LocalDateTime.now());
        this.rank = votes / Math.pow((postTime + 2), 1.8);
    }
}