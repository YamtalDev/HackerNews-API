// package com.akamai.MiniHackerNews.service.impl;

// import org.springframework.cache.Cache;
// import org.springframework.cache.CacheManager;
// import org.springframework.stereotype.Service;

// import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;

// @Service
// public class TopPostsCacheService
// {
//     private final CacheManager cacheManager;

//     public TopPostsCacheService(CacheManager cacheManager)
//     {
//         this.cacheManager = cacheManager;
//     }

//     public void cachePost(Long postId, NewsPostResponseDTO post)
//     {
//         Cache cache = cacheManager.getCache("MyCache");
//         if(cache != null)
//         {
//             cache.put(postId, post);
//         }
//     }

//     public void evictPost(Long postId)
//     {
//         Cache cache = cacheManager.getCache("MyCache");
//         if(cache != null)
//         {
//             cache.evict(postId);
//         }
//     }
// }
