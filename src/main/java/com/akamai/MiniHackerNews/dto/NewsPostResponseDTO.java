package com.akamai.MiniHackerNews.dto;

/******************************************************************************
 * @dto : A DTO entity representing the client response.
******************************************************************************/
public class NewsPostResponseDTO
{
    private Long postId;
    private String postedBy;
    private String post;
    private String link;
    private String timeElapsed;
    private int votes;

    public NewsPostResponseDTO(){/* Empty*/}

    /**************************************************************************
     * @GettersNSetters : Defined for the ModelMapper.
    **************************************************************************/
    public int getVotes(){return (votes);}
    public String getPost(){return (post);}
    public String getLink(){return (link);}
    public Long getPostId(){return (postId);}
    public String getTimeElapsed(){return (timeElapsed);}
    public String getPostedBy(){return (postedBy);}

    public void setLink(String link){this.link = link;}
    public void setPost(String post){this.post = post;}
    public void setVotes(int votes){this.votes = votes;}
    public void setPostId(Long postId){this.postId = postId;}
    public void setPostedBy(String postedBy){this.postedBy = postedBy;}
    public void setTimeElapsed(String timeElapsed){this.timeElapsed = timeElapsed;}
}