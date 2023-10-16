package com.akamai.HackerNews.UnitTests;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import com.akamai.HackerNews.cache.CacheEntity;
import com.akamai.HackerNews.cache.RankedCache;
import com.akamai.HackerNews.dto.NewsPostResponseDTO;

import static org.junit.jupiter.api.Assertions.*;

public class RankedCacheTests
{
    private final int maxTopPostsQueueSize = 30;

    private RankedCache rankedCache;

    @BeforeEach
    public void setUp()
    {
        ConcurrentHashMap<Long, CacheEntity> cacheMap = new ConcurrentHashMap<Long, CacheEntity>();

        PriorityBlockingQueue<CacheEntity> cacheQueue = new PriorityBlockingQueue<CacheEntity>
        (maxTopPostsQueueSize, Comparator.comparingDouble(CacheEntity::getRank).reversed());

        rankedCache = new RankedCache(cacheMap, cacheQueue);
    }

    private NewsPostResponseDTO getNewResponse(long postId)
    {
        NewsPostResponseDTO expectedResponseDTO = new NewsPostResponseDTO();
        expectedResponseDTO.setPostedBy("Test user");
        expectedResponseDTO.setPost("Test post");
        expectedResponseDTO.setLink("http://test.com");
        expectedResponseDTO.setTimeElapsed("Just now");
        expectedResponseDTO.setVotes(0);
        expectedResponseDTO.setPostId(postId);
        return (expectedResponseDTO);
    }

    @Test
    public void testPutAndGet()
    {
        NewsPostResponseDTO newsPost = getNewResponse(1L);
        CacheEntity cacheEntity = new CacheEntity(newsPost, 0.5);

        rankedCache.put(newsPost.getPostId(), cacheEntity);

        CacheEntity retrievedEntity = rankedCache.get(newsPost.getPostId());

        assertNotNull(retrievedEntity);

        assertEquals(cacheEntity.getEntity(), retrievedEntity.getEntity());
        assertEquals(cacheEntity.getRank(), retrievedEntity.getRank());
    }

    // @Test
    // public void testRemove()
    // {
    //     // Create a sample CacheEntity
    //     NewsPostResponseDTO newsPost = new NewsPostResponseDTO();
    //     newsPost.setPostId(1L);
    //     CacheEntity cacheEntity = new CacheEntity(newsPost, 0.5);

    //     // Put the cache entity into the cache
    //     rankedCache.put(1L, cacheEntity);

    //     // Remove the cache entity by key
    //     rankedCache.remove(1L);

    //     // Retrieve the cache entity by key (should be null)
    //     CacheEntity retrievedEntity = rankedCache.get(1L);

    //     // Assert that the retrieved entity is null after removal
    //     assertNull(retrievedEntity);
    // }

    // @Test
    // public void testClear() {
    //     // Create sample CacheEntities and put them into the cache
    //     NewsPostResponseDTO newsPost1 = new NewsPostResponseDTO();
    //     newsPost1.setPostId(1L);
    //     CacheEntity cacheEntity1 = new CacheEntity(newsPost1, 0.5);
    //     NewsPostResponseDTO newsPost2 = new NewsPostResponseDTO();
    //     newsPost2.setPostId(2L);
    //     CacheEntity cacheEntity2 = new CacheEntity(newsPost2, 0.7);
    //     rankedCache.put(1L, cacheEntity1);
    //     rankedCache.put(2L, cacheEntity2);

    //     // Clear the cache
    //     rankedCache.clear();

    //     // Retrieve the cache entities by keys (should be null)
    //     CacheEntity retrievedEntity1 = rankedCache.get(1L);
    //     CacheEntity retrievedEntity2 = rankedCache.get(2L);

    //     // Assert that both retrieved entities are null after clearing
    //     assertNull(retrievedEntity1);
    //     assertNull(retrievedEntity2);
    // }

    // @Test
    // public void testToList() {
    //     // Create sample CacheEntities and put them into the cache
    //     NewsPostResponseDTO newsPost1 = new NewsPostResponseDTO();
    //     newsPost1.setPostId(1L);
    //     CacheEntity cacheEntity1 = new CacheEntity(newsPost1, 0.5);
    //     NewsPostResponseDTO newsPost2 = new NewsPostResponseDTO();
    //     newsPost2.setPostId(2L);
    //     CacheEntity cacheEntity2 = new CacheEntity(newsPost2, 0.7);
    //     rankedCache.put(1L, cacheEntity1);
    //     rankedCache.put(2L, cacheEntity2);

    //     // Retrieve a list of cache entities in descending order of rank
    //     List<NewsPostResponseDTO> cacheList = rankedCache.toList();

    //     // Assert that the list is not null and contains the cache entities in the correct order
    //     assertNotNull(cacheList);
    //     assertEquals(2, cacheList.size());
    //     assertEquals(2L, cacheList.get(0).getPostId());
    //     assertEquals(1L, cacheList.get(1).getPostId());
    // }
}
