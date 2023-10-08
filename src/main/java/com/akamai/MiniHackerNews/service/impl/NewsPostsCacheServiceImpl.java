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

import java.util.List;
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
    private ConcurrentSkipListMap<Long, NewsPostSchema> cache;

    public NewsPostsCacheServiceImpl(ModelMapper modelMapper)
    {
        cache = new ConcurrentSkipListMap<Long, NewsPostSchema>((postId1, postId2) ->
        {
            double rank1 = cache.get(postId1).getRank();
            double rank2 = cache.get(postId2).getRank();
            return (Double.compare(rank2, rank1));
        });

        this.modelMapper = modelMapper;
    }

    @Override
    public NewsPostResponseDTO get(Long key)
    {
        return (modelMapper.map(cache.get(key), NewsPostResponseDTO.class));
    }

    @Override
    public void put(NewsPostSchema value)
    {
        cache.put(value.getPostId(), value);
    }

    @Override
    public void evict(NewsPostSchema value)
    {
        cache.remove(value.getPostId());
    }

    @Override
    public void evictAll()
    {
        cache.clear();
    }

    @Override
    public List<NewsPostResponseDTO> getTopPostsFromCache()
    {
        List<NewsPostResponseDTO> topPosts = cache.values().stream()
        .map(newsPostSchema -> modelMapper.map(newsPostSchema, NewsPostResponseDTO.class))
        .collect(Collectors.toList());

        return (topPosts);
    }

    public void putTopPosts(List<NewsPostSchema> topPosts)
    {
        for(NewsPostSchema topPost : topPosts)
        {
            put(topPost);
        }
    }
}
