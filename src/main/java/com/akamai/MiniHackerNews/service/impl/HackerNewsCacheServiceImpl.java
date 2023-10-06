package com.akamai.MiniHackerNews.service.impl;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Service;

import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;
import com.akamai.MiniHackerNews.service.HackerNewsCacheService;

@Service
public class HackerNewsCacheServiceImpl implements HackerNewsCacheService 
{
    private final ConcurrentLinkedQueue<NewsPostResponseDTO> topPostsCache;
    private final Map<String, NewsPostResponseDTO> cache;

    public HackerNewsCacheServiceImpl
    (ConcurrentHashMap<String, NewsPostResponseDTO> cache, 
    ConcurrentLinkedQueue<NewsPostResponseDTO> topPostsCache)
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
    public void putAllTopPosts(List<NewsPostResponseDTO> topPosts)
    {
        topPostsCache.addAll(topPosts);
    }

    @Override
    public List<NewsPostResponseDTO> getTopPostsFromCache()
    {
        return (new ArrayList<NewsPostResponseDTO>(topPostsCache));
    }

    public void putTopPost(NewsPostResponseDTO topPosts)
    {
        
    }
}

