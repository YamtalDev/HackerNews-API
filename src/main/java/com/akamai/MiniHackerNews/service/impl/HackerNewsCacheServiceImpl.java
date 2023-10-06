package com.akamai.MiniHackerNews.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.akamai.MiniHackerNews.service.HackerNewsCacheService;

@Service
public class HackerNewsCacheServiceImpl implements HackerNewsCacheService 
{
    private final Map<String, Object> cache;

    public HackerNewsCacheServiceImpl(ConcurrentHashMap<String, Object> cache)
    {
        this.cache = cache;
    }

    @Override
    public Object get(String key)
    {
        return (cache.get(key));
    }

    @Override
    public void put(String key, Object value)
    {
        cache.put(key, value);
    }

    @Override
    public void evict(String key)
    {
        cache.remove(key);
    }
}

