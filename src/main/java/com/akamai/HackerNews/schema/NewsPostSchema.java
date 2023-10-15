/******************************************************************************

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

package com.akamai.HackerNews.schema;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.NoArgsConstructor;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.URL;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/******************************************************************************
 * @description: NewsPostSchema representation of a news post. Each news post post 
 * id, contains a title, text link creation time, elapsed time, rank and vote counts.
 * 
 * @indexing: The schema is using indexing on the rank value to gain more efficiency 
 * while retrieving the top pots ranked by this number.
 *
 * @annotations: The schema uses DynamicUpdate to Specifies that SQL update statements
 * include columns which are actually being updated.
 * 
 * @apiNote: The data schema contains PreUpdate and PrePersist methods. Also methods 
 * to upvote, downvote and update rank/elapsed time.
******************************************************************************/

@Data
@Entity
@DynamicUpdate
@NoArgsConstructor
@Table(name = "Posts", indexes = @Index(name = "rank_index", columnList = "rank DESC"))
/******************************************************************************
 * News post model represents a post.
 * Each news post contains a title, text, creation time, updated time, and vote counts.
******************************************************************************/
public class NewsPostSchema
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @NotBlank(message = "User name is required")
    @Column(name = "posted_by", updatable = true)
    @Size(min = 1, max = 20, message = "User name must be between 3 to 20 characters")
    private String postedBy;

    @NotBlank(message = "Post is required")
    @Column(name = "post", updatable = true)
    @Size(min = 1, max = 1024, message = "Post must be between 1 and 1024 characters")
    private String post;

    @Column(name = "link", updatable = true)
    @NotBlank(message = "Link url is required.")
    @URL(message = "Invalid URL. Please provide a valid HTTP or HTTPS URL.")
    @Size(min = 10, max = 1024, message = "Link must be between 10 to 1024 characters long")
    private String link;

    @CreatedDate
    @Column(name = "creation_time", updatable = true)
    private LocalDateTime creationTime;

    @NotBlank(message = "Time elapsed is required")
    @Column(name = "time_elapsed", updatable = true)
    @Size(min = 3, max = 20, message = "Time elapsed must be between 3 to 20 characters long")
    private String timeElapsed;

    @Column(name = "rank", updatable = true)
    @Min(value = 0, message = "Rank must be a non-negative value")
    @Max(value = Long.MAX_VALUE, message = "Maximum rank reached")
    private double rank;

    @Column(name = "votes", updatable = true)
    @Min(value = 0, message = "Votes must be a non-negative value")
    @Max(value = Integer.MAX_VALUE, message = "Maximum votes reached")
    private int votes;

    /**************************************************************************
     * @GettersNSetters : Implementation for the ModelMapper to map between DTO entities.
    **************************************************************************/
    public int getVotes(){ return (votes);}
    public double getRank(){ return (rank);}
    public String getPost(){ return (post);}
    public Long getPostId(){ return (postId);}
    public String getPostedBy(){ return (postedBy);}
    public String getTimeElapsed(){ return (timeElapsed);}

    public void setPost(String post){ this.post = post;}
    public void setLink(String link){ this.link = link;}
    public void setPostedBy(String posted_by){ this.postedBy = posted_by;}

    /**************************************************************************
     * @Voting: Methods to upvote and downvote the post.
    **************************************************************************/
    public void upVote()
    {
        ++this.votes;
    }

    public void downVote()
    {
        --this.votes;
    }

    /**************************************************************************
     * @Initiation: Every time a post will be constructed this method will be triggered.
    **************************************************************************/
    @PrePersist
    public void setTime()
    {
        timeElapsed = "Just now";
        this.creationTime = LocalDateTime.now();
    }

    /**************************************************************************
     * @Updating : Every time a post will be updated this method will be triggered.
     *           : The updated values are the rank and the elapsed time.  
     *           : This method is using the `Hot Post` ranking algorithm. See link below:
     * @link     : https://medium.com/hacking-and-gonzo/how-hacker-news-ranking-algorithm-works-1d9b0cf2c08d
     **************************************************************************/
    @PreUpdate
    public void update()
    {
        long hoursFromCreation = ChronoUnit.HOURS.between(creationTime, LocalDateTime.now());
        this.rank = calculateRank(hoursFromCreation);
        updateTimeElapsed(hoursFromCreation);
    }

    private double calculateRank(long hoursFromCreation)
    {
        return (votes / Math.pow((hoursFromCreation + 2), 1.8));
    }

    /**************************************************************************
     * @time: Represent the time elapsed as a string for the client(Just like Hacker news displays it).
    **************************************************************************/
    public void updateTimeElapsed(long hoursFromCreation)
    {
        if(1 > hoursFromCreation)
        {
            timeElapsed = "Just now";
        }
        else
        {
            long timeValue = 0;
            String timeUnit = "";
            if(hoursFromCreation < 24)
            {
                timeValue = hoursFromCreation;
                timeUnit = "hour";
            }
            else
            {
                timeValue = hoursFromCreation / 24;
                timeUnit = "day";
            }

            this.timeElapsed = timeValue + " " + timeUnit + (timeValue == 1 ? "" : "s") + " ago";
        }
    }
}