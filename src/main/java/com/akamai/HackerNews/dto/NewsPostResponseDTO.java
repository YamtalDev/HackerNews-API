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

package com.akamai.HackerNews.dto;

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

    public NewsPostResponseDTO()
    {
        // Empty
    }

    /**************************************************************************
     * @GettersNSetters : Defined for ModelMapper.
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