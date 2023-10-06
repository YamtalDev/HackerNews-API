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
        List<?> topPosts = (List<?>)value;

        for(int i = 0; i < topPosts.size(); i++)
        {
            Object listItem = topPosts.get(i);
            if(listItem instanceof NewsPostResponseDTO)
            {
                NewsPostResponseDTO post = (NewsPostResponseDTO) listItem;
                if (key.equals(String.valueOf(post.getPostId())))
                {
                    topPosts.set(i, value);
                    break;
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

