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

    public Object get(String key)
    {
        return (cache.get(key));
    }

    // public void put(String key, Object value)
    // {
    //     Object cachedPageObj = get("top-posts");

    //     if(cachedPageObj != null)
    //     {
    //         if (cachedPageObj instanceof Page<?>)
    //         {
    //             Page<NewsPostResponseDTO> cachedPage = (Page<NewsPostResponseDTO>) cachedPageObj;
        
    //             int index = findIndexOfEntityInPage(cachedPage, key);
    //             if(index >= 0)
    //             {
    //                 List<NewsPostResponseDTO> content = new ArrayList<>(cachedPage.getContent());
    //                 content.set(index, (NewsPostResponseDTO) value);
    //                 Page<NewsPostResponseDTO> updatedPage = new PageImpl<>(content, cachedPage.getPageable(), cachedPage.getTotalElements());
    //                 cache.put("top-posts", updatedPage);
    //             }
    //             else
    //             {
    //                 cache.put(key, value);
    //             }
    //         }
    //         else
    //         {
    //             cache.put(key, value);
    //         }
    //     }
    // }

    public void put(String key, Object value)
    {
        cache.put(key, value);
    }

    public void evict(String key)
    {
        cache.remove(key);
    }

    // Method to update a specific entity within a cached page
    public void updateEntityInCachedPage(String key, int index, NewsPostResponseDTO updatedEntity)
    {
        Object cachedPage = get(key);

        if(cachedPage instanceof Page<?>)
        {
            Page<NewsPostResponseDTO> page = (Page<NewsPostResponseDTO>) cachedPage;
            List<NewsPostResponseDTO> content = new ArrayList<>(page.getContent());
            
            // Update the specific entity at the given index
            if (index >= 0 && index < content.size()) {
                content.set(index, updatedEntity);
            }

            // Create a new page with the updated content
            Page<NewsPostResponseDTO> updatedPage = new PageImpl<>(content, page.getPageable(), page.getTotalElements());
            
            // Put the updated page back into the cache with the same key
            put(key, updatedPage);
        }
    }
}

