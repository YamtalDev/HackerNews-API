package com.akamai.MiniHackerNews.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;

@Service
public class HackerNewsCacheService 
{
    private final Map<String, Object> cache;

    public HackerNewsCacheService(ConcurrentHashMap<String, Object> cache)
    {
        this.cache = cache;
    }

    public void put(String key, Object value)
    {
        cache.put(key, value);
    }

    public void evict(String key)
    {
        cache.remove(key);
    }

    public void updateEntityInCachedPage(String key, int index, NewsPostResponseDTO updatedEntity)
    {
        Object cachedPage = cache.get(key);

        if(cachedPage instanceof Page<?>)
        {
            Page<NewsPostResponseDTO> page = (Page<NewsPostResponseDTO>) cachedPage;
            List<NewsPostResponseDTO> content = new ArrayList<>(page.getContent());
            
            if (index >= 0 && index < content.size())
            {
                content.set(index, updatedEntity);
            }

            Page<NewsPostResponseDTO> updatedPage = new PageImpl<>(content, page.getPageable(), page.getTotalElements());
            put(key, updatedPage);
        }
    }
}


