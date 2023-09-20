package com.akamai.MiniHackerNews.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.akamai.MiniHackerNews.schema.NewsPost;
import com.akamai.MiniHackerNews.dto.NewsPostUserRequestDTO;

/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
 * @description:  
******************************************************************************/

public interface NewsPostService
{
    void deletePost(Long post_id);
    NewsPost getPostById(Long post_id);
    public NewsPost upvotePost(Long post_id);
    public NewsPost downvotePost(Long post_id);
    Page<NewsPost> getAllPosts(Pageable pageable);
    NewsPost saveNewsPost(NewsPostUserRequestDTO newsPost);
    NewsPost updatePost(NewsPostUserRequestDTO newPost, Long post_id);
}
