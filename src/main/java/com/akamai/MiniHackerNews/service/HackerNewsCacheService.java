package com.akamai.MiniHackerNews.service;

public interface HackerNewsCacheService 
{
    public void evict(String key);
    public void put(String key, Object value);
    public Object get(String key);
}
