package com.akamai.MiniHackerNews.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Configuration;

import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;

@Configuration
public class CacheConfig implements CachingConfigurer
{
    @Bean
    public ConcurrentHashMap<String, NewsPostResponseDTO> cache()
    {
        return (new ConcurrentHashMap<String, NewsPostResponseDTO>());
    }

    @Bean
    public CopyOnWriteArrayList<NewsPostResponseDTO> topPostsCache()
    {
        return new CopyOnWriteArrayList<>();
    }
}