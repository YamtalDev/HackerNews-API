package com.akamai.MiniHackerNews.schema.dto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class NewsPostResponseDTO
{
    private Long postId;
    private String postedBy;
    private String post;
    private String link;
    private String time;
    private int votes;

    public NewsPostResponseDTO()
    {
        // Empty
    }

    public int getVotes()
    {
        return (votes);
    }

    public String getPost()
    {
        return (post);
    }

    public String getLink()
    {
        return (link);
    }

    public Long getPostId()
    {
        return (postId);
    }

    public String getTime()
    {
        return (time);
    }

    public String getPostedBy()
    {
        return (postedBy);
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public void setPost(String post)
    {
        this.post = post;
    }

    public void setVotes(int votes)
    {
        this.votes = votes;
    }

    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

    public void setPostedBy(String postedBy)
    {
        this.postedBy = postedBy;
    }

    public void setTime(LocalDateTime time)
    {
        long timeElapsed = ChronoUnit.HOURS.between(time, LocalDateTime.now());
        if(timeElapsed < 1)
        {
            this.time = "Just now";
        }
        else
        {
            long timeValue = 0;
            String timeUnit = "";
            if(timeElapsed < 24)
            {
                timeValue = timeElapsed;
                timeUnit = "hour";
            }
            else
            {
                timeValue = timeElapsed / 24;
                timeUnit = "day";
            }

            this.time = timeValue + " " + timeUnit + (timeValue == 1 ? "" : "s") + " ago";
        }
    }
}