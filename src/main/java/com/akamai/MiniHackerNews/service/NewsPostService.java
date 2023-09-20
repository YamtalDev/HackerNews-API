package com.akamai.MiniHackerNews.service;

import java.util.List;
import com.akamai.MiniHackerNews.model.NewsPost;

public interface NewsPostService
{
    List<NewsPost> getAllPosts();
    void deletePost(Long post_id);
    NewsPost getPostById(Long post_id);
    NewsPost saveNewsPost(NewsPost newsPost);
    NewsPost updatePost(NewsPost newPost, Long post_id);
}
