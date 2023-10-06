package com.akamai.MiniHackerNews.service;

import java.util.List;

import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;
import com.akamai.MiniHackerNews.schema.NewsPostSchema;

public interface HackerNewsCacheService 
{
    public void evict(String key);
    public NewsPostResponseDTO get(String key);
    public void put(String key, NewsPostResponseDTO value);

    public void evictTopPostsFromCache();
    public List<NewsPostResponseDTO> getTopPostsFromCache();
    public void putAllTopPosts(List<NewsPostSchema> topPosts);
    public void putTopPost(NewsPostResponseDTO topPost, double rank);
}
