package com.akamai.MiniHackerNews.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

//import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;

@Service
public class HackerNewsCacheService 
{
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public Object get(String key)
    {
        return (cache.get(key));
    }

    public void put(String key, Object value)
    {
        cache.put(key, value);
    }

    public void evict(String key)
    {
        cache.remove(key);
    }
}
