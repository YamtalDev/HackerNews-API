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

package com.akamai.HackerNews.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.akamai.HackerNews.cache.CacheEntity;
import com.akamai.HackerNews.cache.RankedCache;
import com.akamai.HackerNews.dto.NewsPostResponseDTO;
import com.akamai.HackerNews.schema.NewsPostSchema;
import com.akamai.HackerNews.service.NewsPostsCacheService;

/******************************************************************************
 * @description: Implementation of the NewsPostsCacheService responsible for 
 * caching and managing news posts. This service utilizes a RankedCache to 
 * store and retrieve news posts and their ranks.
******************************************************************************/
@Service
public class NewsPostsCacheServiceImpl implements NewsPostsCacheService
{
    private final RankedCache cache;
    private final ModelMapper modelMapper;

    /**************************************************************************
     * @description       : Constructs a NewsPostsCacheServiceImpl instance.
     * @param cache       : The RankedCache used for caching news posts.
     * @param modelMapper : A ModelMapper for mapping NewsPostSchema to NewsPostResponseDTO.
    **************************************************************************/
    public NewsPostsCacheServiceImpl(RankedCache cache, ModelMapper modelMapper)
    {
        this.cache = cache;
        this.modelMapper = modelMapper;
    }

    /**************************************************************************
     * @description  : Adds a news post and its rank to the cache.
     * @param entity : The NewsPostResponseDTO representing the news post.
     * @param rank   : The rank of the news post for ordering purposes.
    **************************************************************************/
    @Override
    public void put(NewsPostResponseDTO entity, Double rank)
    {
        cache.put(entity.getPostId(), new CacheEntity(entity, rank));
    }

    /**************************************************************************
     * @description  : Evicts a specific news post from the cache by its ID.
     * @param postId : The ID of the news post to remove from the cache.
    **************************************************************************/
    @Override
    public void evict(Long postId)
    {
        cache.remove(postId);
    }

    /**************************************************************************
     * @description  : Retrieves a cached news post by its ID.
     * @param postId : The ID of the news post to retrieve.
     * @return       : The cached NewsPostResponseDTO if found; otherwise, returns null.
    **************************************************************************/
    @Override
    public NewsPostResponseDTO get(Long postId)
    {
        CacheEntity cacheEntity = cache.get(postId);
        return cacheEntity != null ? cacheEntity.getEntity() : null;
    }

    /**************************************************************************
     * @description: Clears the entire cache, removing all cached news posts.
    **************************************************************************/
    @Override
    public void evictAll()
    {
        cache.clear();
    }

    /**************************************************************************
     * @description    : Adds a list of top news posts to the cache.
     * @param topPosts : A list of NewsPostSchema representing the top news posts.
    **************************************************************************/
    @Override
    public void putTopPosts(List<NewsPostSchema> topPosts)
    {
        for(NewsPostSchema topPost: topPosts)
        {
            put(modelMapper.map(topPost, NewsPostResponseDTO.class), topPost.getRank());
        }
    }

    /**************************************************************************
     * @description : Retrieves a list of top news posts from the cache.
     * @return      : A list of cached NewsPostResponseDTO representing top news posts.
    **************************************************************************/
    @Override
    public List<NewsPostResponseDTO> getTopPostsFromCache()
    {
        return (cache.toList());
    }
}