package com.akamai.MiniHackerNews.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.akamai.MiniHackerNews.schema.NewsPostEntity;
import com.akamai.MiniHackerNews.schema.dto.NewsPostRequest;
import com.akamai.MiniHackerNews.schema.dto.NewsPostResponse;

/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
 * @description:  
******************************************************************************/

public interface NewsPostService
{
    public int upvotePost(Long post_id);
    public void deletePost(Long post_id);
    public int downvotePost(Long post_id);
    public NewsPostResponse getPostById(Long post_id);
    public NewsPostEntity getPostEntityById(Long post_id);
    public Page<NewsPostResponse> getAllPosts(Pageable pageable);
    public NewsPostResponse saveNewsPost(NewsPostRequest newsPost);
    public Page<NewsPostResponse> getPostsByRankDesc(Pageable pageable);
    public NewsPostResponse updatePost(NewsPostRequest newPost, Long post_id);
}
