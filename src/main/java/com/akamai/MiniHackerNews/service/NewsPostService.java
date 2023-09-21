package com.akamai.MiniHackerNews.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.akamai.MiniHackerNews.schema.NewsPost;
import com.akamai.MiniHackerNews.dto.NewsPostRequestDTO;

/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
 * @description:  
******************************************************************************/

public interface NewsPostService
{
    public List<NewsPost> getTopPosts();
    public void deletePost(Long post_id);
    public NewsPost getPostById(Long post_id);
    public NewsPost upvotePost(Long post_id);
    public NewsPost downvotePost(Long post_id);
    public Page<NewsPost> getAllPosts(Pageable pageable);
    public NewsPost saveNewsPost(NewsPostRequestDTO newsPost);
    public NewsPost updatePost(NewsPostRequestDTO newPost, Long post_id);
}
