package com.akamai.MiniHackerNews.service;

import java.util.List;
import com.akamai.MiniHackerNews.model.NewsPost;

public interface NewsPostService
{
    List<NewsPost> getAllPosts();
    void deletePost(Long post_id);
    NewsPost getPostById(Long post_id);
    NewsPost saveNewsPost(NewsPost newsPost);
    public NewsPost upvotePost(Long post_id);
    public NewsPost downvotePost(Long post_id);
    NewsPost updatePost(NewsPost newPost, Long post_id);
}
