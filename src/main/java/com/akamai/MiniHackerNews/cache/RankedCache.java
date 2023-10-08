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

package com.akamai.MiniHackerNews.cache;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;

import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;

public class RankedCache
{
    @Value("${app.cache.size}")
    private int maxCacheSize;

    @Value("${app.cache.top-posts-size}")
    private int maxTopPostsQueueSize;

    private double lowestRank;
    private final ConcurrentHashMap<Long, CacheEntity> cacheMap;
    private final PriorityBlockingQueue<CacheEntity> cacheQueue;

    public RankedCache(ConcurrentHashMap<Long, CacheEntity> cacheMap, PriorityBlockingQueue<CacheEntity> cacheQueue)
    {
        this.lowestRank = 0;
        this.cacheMap = cacheMap;
        this.cacheQueue = cacheQueue;
    }

    public void put(Long key, CacheEntity value)
    {
        CacheEntity existingEntity = cacheMap.remove(key);
        if(null != existingEntity)
        {
            cacheQueue.remove(existingEntity);
        }

        if(maxTopPostsQueueSize <= cacheQueue.size() && lowestRank < value.getRank())
        {
            removeLowestRank();
            cacheQueue.put(value);
        }

        cacheMap.put(key, value);
    }

    public CacheEntity get(Long key)
    {
        return (cacheMap.get(key));
    }

    public void clear()
    {
        cacheMap.clear();
        cacheQueue.clear();
    }

    public void remove(Long key)
    {
        CacheEntity removedValue = cacheMap.remove(key);

        if(null != removedValue)
        {
            cacheQueue.remove(removedValue);
        }
    }

    public List<NewsPostResponseDTO> toList()
    {
        return (cacheQueue.stream().map(CacheEntity::getEntity).collect(Collectors.toList()));
    }

    private void removeLowestRank()
    {
        CacheEntity lowestRankedEntity = getLowestRankedEntity();
        if(lowestRankedEntity != null)
        {
            lowestRank = lowestRankedEntity.getRank();
            cacheQueue.remove(lowestRankedEntity);
            cacheMap.remove(lowestRankedEntity.getEntity().getPostId());
        }
    }

    private CacheEntity getLowestRankedEntity()
    {
        Iterator<CacheEntity> iterator = cacheQueue.iterator();
        CacheEntity lowestRankedEntity = null;
        while(iterator.hasNext())
        {
            lowestRankedEntity = iterator.next();
        }

        return (lowestRankedEntity);
    }
}