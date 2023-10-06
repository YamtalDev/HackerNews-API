package com.akamai.MiniHackerNews.service.impl;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;
import com.akamai.MiniHackerNews.schema.NewsPostSchema;
import com.akamai.MiniHackerNews.service.HackerNewsCacheService;

@Service
public class HackerNewsCacheServiceImpl implements HackerNewsCacheService 
{
    private ModelMapper modelMapper;
    private final Map<String, NewsPostResponseDTO> cache;
    private final ConcurrentSkipListMap<Double, NewsPostResponseDTO> topPostsCache;
    private final Comparator<Double> descendingComparator = (rank1, rank2) -> Double.compare(rank2, rank1);

    public HackerNewsCacheServiceImpl
    (ModelMapper modelMapper, ConcurrentHashMap<String, NewsPostResponseDTO> cache)
    {
        this.cache = cache;
        this.modelMapper = modelMapper;
        this.topPostsCache = new ConcurrentSkipListMap<Double, NewsPostResponseDTO>(descendingComparator);
    }

    @Override
    public NewsPostResponseDTO get(String key)
    {
        return cache.get(key);
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
        topPostsCache.clear();
    }

    @Override
    public void putAllTopPosts(List<NewsPostSchema> topPosts)
    {
        for(NewsPostSchema post : topPosts)
        {
            topPostsCache.put(post.getRank(), modelMapper.map(post, NewsPostResponseDTO.class));
        }
    }

    @Override
    public List<NewsPostResponseDTO> getTopPostsFromCache()
    {
        return (new ArrayList<NewsPostResponseDTO>(topPostsCache.values()));
    }

    public void putTopPost(NewsPostResponseDTO topPost, double rank)
    {
        topPostsCache.entrySet()
        .removeIf(entry -> entry.getValue().getPostId().equals(topPost.getPostId()));
    
        topPostsCache.put(rank, topPost);
    }
    
}
