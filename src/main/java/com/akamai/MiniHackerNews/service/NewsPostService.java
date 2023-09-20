package com.akamai.MiniHackerNews.service;

import java.util.List;

import com.akamai.MiniHackerNews.dto.NewsPostUserRequestDTO;
import com.akamai.MiniHackerNews.schema.NewsPost;

public interface NewsPostService
{
    List<NewsPost> getAllPosts();
    void deletePost(Long post_id);
    NewsPost getPostById(Long post_id);
    public NewsPost upvotePost(Long post_id);
    public NewsPost downvotePost(Long post_id);
    NewsPost saveNewsPost(NewsPostUserRequestDTO newsPost);
    NewsPost updatePost(NewsPostUserRequestDTO newPost, Long post_id);
}
