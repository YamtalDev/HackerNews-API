package com.akamai.MiniHackerNews.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig implements CachingConfigurer
{
    @Bean
    public ConcurrentHashMap<String, Object> cache()
    {
        return new ConcurrentHashMap<String, Object>();
    }
}