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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;
import com.akamai.MiniHackerNews.schema.NewsPostSchema;
import com.akamai.MiniHackerNews.service.NewsPostsCacheService;

@Service
public class NewsPostsCacheServiceImpl implements NewsPostsCacheService 
{
    private ModelMapper modelMapper;
    private ConcurrentSkipListMap<Double, Long> rankToPostIdMap;
    private Map<Long, NewsPostResponseDTO> cache;

    public NewsPostsCacheServiceImpl(ModelMapper modelMapper, ConcurrentSkipListMap<Double, Long> rankToPostIdMap, ConcurrentHashMap<Long, NewsPostResponseDTO> cache)
    {
        this.cache = cache;
        this.modelMapper = modelMapper;
        this.rankToPostIdMap = rankToPostIdMap;
    }


    @Override
    public NewsPostResponseDTO get(Long postId)
    {
        return cache.get(postId);
    }

    @Override
    public void put(NewsPostResponseDTO value, Double rank)
    {
        Long postId = value.getPostId()
        rankToPostIdMap.put(rank, postId);
        cache.put(postId, value);
    }

    @Override
    public void evict(Long postId)
    {
        cache.remove(cache.get(postId).getRank());
        cache.remove(postId);
    }

    @Override
    public void evictAll()
    {
        rankToPostIdMap.clear();
        cache.clear();
    }

    @Override
    public List<NewsPostResponseDTO> getTopPostsFromCache()
    {
        List<Long> sortedPostIds = new ArrayList<Long>(rankToPostIdMap.values());
        
        return sortedPostIds.stream()
            .map(postId -> cache.get(postId))
            .collect(Collectors.toList());
    }

    public void putTopPosts(List<NewsPostSchema> topPosts)
    {
        for(NewsPostSchema topPost: topPosts)
        {
            put(modelMapper.map(topPost, NewsPostResponseDTO.class), topPost.getRank());
        }
    }
}
