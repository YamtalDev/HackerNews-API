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

package com.akamai.HackerNews.config;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.akamai.HackerNews.cache.CacheEntity;
import com.akamai.HackerNews.cache.RankedCache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurer;


/******************************************************************************
 * @description: Configuration class for setting up the caching infrastructure.
 * This class defines the configuration for caching in the application, including
 * cache size, queue size, and the creation of the RankedCache bean.
******************************************************************************/
@Configuration
public class CacheConfig implements CachingConfigurer
{
    @Value("${app.cache.top-posts-size}")
    private int maxTopPostsQueueSize;

    @Value("${app.cache.size}")
    private int maxCacheSize;

    /**************************************************************************
     * @description: Configure a ConcurrentHashMap to store cache entries.
     * @return A ConcurrentHashMap for storing cache entries.
    **************************************************************************/
    @Bean
    public ConcurrentHashMap<Long, CacheEntity> cacheMap()
    {
        return (new ConcurrentHashMap<Long, CacheEntity>(maxCacheSize));
    }

    /**************************************************************************
     * @description: Configure a PriorityBlockingQueue to manage cache entries based on rank.
     * @return A PriorityBlockingQueue for managing cache entries in descending rank order.
    **************************************************************************/
    @Bean
    public PriorityBlockingQueue<CacheEntity> cacheQueue()
    {
        return (new PriorityBlockingQueue<CacheEntity>
        (maxTopPostsQueueSize, Comparator.comparingDouble(CacheEntity::getRank)));
    }

    /**************************************************************************
     * @description: Creates and configures a RankedCache bean that uses the 
     * provided cacheMap and cacheQueue.
     *
     * @param cacheMap   ConcurrentHashMap for storing cache entries.
     * @param cacheQueue PriorityBlockingQueue for managing cache entries based on rank.
     * @return           A RankedCache bean for caching and managing data.
    **************************************************************************/
    @Bean
    public RankedCache rankedCache
    (ConcurrentHashMap<Long, CacheEntity> cacheMap,
    PriorityBlockingQueue<CacheEntity> cacheQueue)
    {
        return (new RankedCache(cacheMap, cacheQueue));
    }
}