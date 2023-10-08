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

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.akamai.MiniHackerNews.cache.CacheEntity;
import com.akamai.MiniHackerNews.cache.RankedCache;
import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;
import com.akamai.MiniHackerNews.schema.NewsPostSchema;
import com.akamai.MiniHackerNews.service.NewsPostsCacheService;

@Service
public class NewsPostsCacheServiceImpl implements NewsPostsCacheService
{
    private final RankedCache cache;
    private final ModelMapper modelMapper;
 
    public NewsPostsCacheServiceImpl(RankedCache cache, ModelMapper modelMapper)
    {
        this.cache = cache;
        this.modelMapper = modelMapper;
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
    public NewsPostResponseDTO get(Long postId)
    {
        CacheEntity cacheEntity = cache.get(postId);
        return cacheEntity != null ? cacheEntity.getEntity() : null;
    }

    @Override
    public void evictAll()
    {
        cache.clear();
    }

    @Override
    public void putTopPosts(List<NewsPostSchema> topPosts)
    {
        for(NewsPostSchema topPost : topPosts)
        {
            put(modelMapper.map(topPost, NewsPostResponseDTO.class), topPost.getRank());
        }
    }

    @Override
    public List<NewsPostResponseDTO> getTopPostsFromCache()
    {
        return (cache.toList());
    }
}