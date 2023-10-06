package com.akamai.MiniHackerNews.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;
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
        Object cachedValue = cache.get("top-posts");
        if(cachedValue != null && cachedValue instanceof List<?>)
        {
            List<NewsPostResponseDTO> topPosts = (List<NewsPostResponseDTO>) cachedValue;
            for(int i = 0; i < topPosts.size(); i++)
            {
                System.out.println("HAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHA");
                if(topPosts.get(i).getPostId().equals(((NewsPostResponseDTO) value).getPostId()))
                {
                    topPosts.set(i, (NewsPostResponseDTO) value);
                    cache.put("top-posts", topPosts);
                    return;
                }
            }
        }

        cache.put(key, value);
    }



    @Override
    public void evict(String key)
    {
        cache.remove(key);
    }
}

