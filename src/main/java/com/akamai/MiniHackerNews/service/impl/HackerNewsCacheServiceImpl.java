/******************************************************************************

Copyright (c) 2023 Tal Aharon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

******************************************************************************/

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
        return (cache.get(key));
    }

    @Override
    public void put(String key, NewsPostResponseDTO value)
    {
        cache.put(key, value);
    }

    @Override
    public void evict(String key, NewsPostResponseDTO value)
    {
        if(null == cache.remove(key))
        {
            evictTopPostFromCache(value);
        }
    }

    private void evictTopPostFromCache(NewsPostResponseDTO topPost)
    {
        topPostsCache.entrySet()
        .removeIf(entry -> entry.getValue()
        .getPostId().equals(topPost.getPostId()));
    }

    @Override
    public void evictAllTopPostsFromCache()
    {
        topPostsCache.clear();
    }

    @Override
    public void putTopPosts(List<NewsPostSchema> topPosts)
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
        evictTopPostFromCache(topPost);
        topPostsCache.put(rank, topPost);
    }
    
}
