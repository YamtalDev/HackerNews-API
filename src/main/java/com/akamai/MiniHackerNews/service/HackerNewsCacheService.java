package com.akamai.MiniHackerNews.service;

import java.util.List;

import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;

public interface HackerNewsCacheService 
{
    public void evict(String key);
    public NewsPostResponseDTO get(String key);
    public void put(String key, NewsPostResponseDTO value);

    public void evictTopPostsFromCache();
    public void putTopPost(NewsPostResponseDTO topPosts);
    public List<NewsPostResponseDTO> getTopPostsFromCache();
    public void putAllTopPosts(List<NewsPostResponseDTO> topPosts);
}
