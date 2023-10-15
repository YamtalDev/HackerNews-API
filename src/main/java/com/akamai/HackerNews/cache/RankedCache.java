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

package com.akamai.HackerNews.cache;

import java.util.List;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.beans.factory.annotation.Value;

import com.akamai.HackerNews.dto.NewsPostResponseDTO;

/******************************************************************************
 * @description: This thread-safe ranked cache data structure orders entities in 
 * descending order based on their rank values. It maintains a fixed size cache 
 * and reflects the most up-to-date data from the database.
******************************************************************************/
public class RankedCache
{
    // Inject a configurable variable from the properties file.
    @Value("${app.cache.size}")
    private int maxCacheSize;

    // Inject a configurable variable from the properties file.
    @Value("${app.cache.top-posts-size}")
    private int maxTopPostsQueueSize;

    private double lowestRank;
    private final ConcurrentHashMap<Long, CacheEntity> cacheMap;
    private final PriorityBlockingQueue<CacheEntity> cacheQueue;


    /**************************************************************************
     * @description      : Initializes a new instance of the RankedCache class.
     * @param cacheMap   : The ConcurrentHashMap to store cache entities.
     * @param cacheQueue : The PriorityBlockingQueue to maintain cache entities
     *                   : in descending order of rank.
    **************************************************************************/
    public RankedCache(ConcurrentHashMap<Long, CacheEntity> cacheMap, PriorityBlockingQueue<CacheEntity> cacheQueue)
    {
        this.lowestRank = 0;
        this.cacheMap = cacheMap;
        this.cacheQueue = cacheQueue;
    }

    /**************************************************************************
     * @description : Adds a cache entity with the specified key and value to the cache.
     * @param key   : The key associated with the cache entity.
     * @param value : The cache entity to be added.
    **************************************************************************/
    public void put(Long key, CacheEntity value)
    {
        CacheEntity existingEntity = cacheMap.remove(key);
        if(null != existingEntity)
        {
            cacheQueue.remove(existingEntity);
        }

        if(maxTopPostsQueueSize <= cacheQueue.size() && lowestRank < value.getRank())
        {
            removeLowestRankedEntity();
            cacheQueue.put(value);
        }

        cacheMap.put(key, value);
    }

    /**************************************************************************
     * @description : Retrieves the cache entity with the specified key.
     * @param key   : The key associated with the cache entity to retrieve.
     * @return      : The cache entity associated with the given key, or null.
    **************************************************************************/
    public CacheEntity get(Long key)
    {
        return (cacheMap.get(key));
    }

    /**************************************************************************
     * @description: Clears the entire cache, removing all cache entities.
    **************************************************************************/
    public void clear()
    {
        cacheMap.clear();
        cacheQueue.clear();
    }

    /**************************************************************************
     * @description : Removes the cache entity with the specified key from the cache.
     * @param key   : The key associated with the cache entity to remove.
    **************************************************************************/
    public void remove(Long key)
    {
        CacheEntity removedValue = cacheMap.remove(key);

        if(null != removedValue)
        {
            cacheQueue.remove(removedValue);
        }
    }

    /**************************************************************************
     * @description : Returns a list of cache entities in descending order of rank.
     * @return      : A list of cache entities ordered by rank in descending order.
    **************************************************************************/
    public List<NewsPostResponseDTO> toList()
    {
        return (cacheQueue.stream().map(CacheEntity::getEntity).collect(Collectors.toList()));
    }

    /**************************************************************************
     * @description : Removes the cache entity with the lowest rank from the cache.
     * This method is called when the cache is full and a new entity with a higher rank
     * needs to be added. It ensures that the cache maintains its maximum size and
     * removes the entity with the lowest rank.
    **************************************************************************/
    private void removeLowestRankedEntity()
    {
        CacheEntity lowestRankedEntity = getLowestRankedEntity();
        if(lowestRankedEntity != null)
        {
            lowestRank = lowestRankedEntity.getRank();
            cacheQueue.remove(lowestRankedEntity);
            cacheMap.remove(lowestRankedEntity.getEntity().getPostId());
        }
    }

    /**************************************************************************
     * @description : Retrieves the cache entity with the lowest rank from the cache.
     * This method iterates through the cache queue to find and return the cache entity
     * with the lowest rank. If no such entity is found, it returns null.
     * 
     * @return The cache entity with the lowest rank or null if the cache is empty.
    **************************************************************************/
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