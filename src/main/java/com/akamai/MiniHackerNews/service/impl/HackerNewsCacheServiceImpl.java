package com.akamai.MiniHackerNews.service.impl;

import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;
import com.akamai.MiniHackerNews.service.HackerNewsCacheService;

@Service
public class HackerNewsCacheServiceImpl implements HackerNewsCacheService 
{
    private List<NewsPostResponseDTO> topPostsCache;
    private final Map<String, NewsPostResponseDTO> cache;

    public HackerNewsCacheServiceImpl
    (ConcurrentHashMap<String, NewsPostResponseDTO> cache, 
    CopyOnWriteArrayList<NewsPostResponseDTO> topPostsCache)
    {
        this.topPostsCache = topPostsCache;
        this.cache = cache;
    }

    @Override
    public NewsPostResponseDTO get(String key)
    {
        return (cache.get(key));
    }

    @Override
    public void put(String key, NewsPostResponseDTO value)
    {
        cache.put(key, value);
    }

    @Override
    public void evict(String key)
    {
        cache.remove(key);
    }

    @Override
    public void evictTopPostsFromCache()
    {
        topPostsCache.clear();;
    }

    @Override
    public void putTopPosts(List<NewsPostResponseDTO> topPosts)
    {
        topPostsCache = topPosts;
    }

    @Override
    public List<NewsPostResponseDTO> getTopPostsFromCache()
    {
        return (topPostsCache);
    }
}

