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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.akamai.MiniHackerNews.config.CacheEntity;
import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;
import com.akamai.MiniHackerNews.schema.NewsPostSchema;
import com.akamai.MiniHackerNews.service.NewsPostsCacheService;

@Service
public class NewsPostsCacheServiceImpl implements NewsPostsCacheService 
{
    private ModelMapper modelMapper;
    List<NewsPostResponseDTO> topPosts;
    private ConcurrentMap<Long, CacheEntity> cache;

    public NewsPostsCacheServiceImpl
    (ModelMapper modelMapper, ConcurrentHashMap<Long, CacheEntity> cache, ArrayList<NewsPostResponseDTO> topPosts)
    {
        this.cache = cache;
        this.modelMapper = modelMapper;
        this.topPosts = Collections.synchronizedList(new ArrayList<NewsPostResponseDTO>());
    }

    @Override
    public NewsPostResponseDTO get(Long postId)
    {
        CacheEntity entry = cache.get(postId);
        return (entry != null ? entry.getEntity() : null);
    }

    @Override
    public void put(NewsPostResponseDTO entity, Double rank)
    {
        cache.put(entity.getPostId(), new CacheEntity(entity, rank));
    }

    @Override
    public void evict(Long postId)
    {
        cache.remove(postId);
    }

    @Override
    public void evictAll()
    {
        cache.clear();
    }

    @Override
    public List<NewsPostResponseDTO> getTopPostsFromCache()
    {
        List<CacheEntity> entries = cache.values().stream()
        .sorted((entry1, entry2) -> Double.compare(entry2.getRank(), entry1.getRank()))
        .collect(Collectors.toList());
        
        return entries.stream().map
        (entry -> entry.getEntity()).collect(Collectors.toList());
    }

    public void putTopPosts(List<NewsPostSchema> topPosts)
    {
        for(NewsPostSchema topPost: topPosts)
        {
            put(modelMapper.map(topPost, NewsPostResponseDTO.class), topPost.getRank());
        }
    }
}