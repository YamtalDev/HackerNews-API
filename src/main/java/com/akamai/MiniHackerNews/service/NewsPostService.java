package com.akamai.MiniHackerNews.service;

import java.util.List;
import com.akamai.MiniHackerNews.model.NewsPost;

public interface NewsPostService
{
    NewsPost saveNewsPost(NewsPost newsPost);
    List<NewsPost> getAllPosts();
}
