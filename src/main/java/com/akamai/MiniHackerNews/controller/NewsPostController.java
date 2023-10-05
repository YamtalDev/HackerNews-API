package com.akamai.MiniHackerNews.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akamai.MiniHackerNews.dto.*;
import com.akamai.MiniHackerNews.service.*;

/**************************************************************************
 * @description          : Controller layer crud Implementation for the api.
 * @NewsPostRequestDTO   : DTO representation of the client post, put requests structure.
 * @NewsPostResponseDTO  : DTO to represent the structure of the response to the client side.
 * @NewsUpdateRequestDTO : DTO representation of the client patch request structure.
 *
 * @caching : The controllers use caching mechanism, to improve performance.
 * 
 * @apiNote : GET controllers for all posts and for top posts uses pagination 
*           : for efficiently. GET top posts page size is configurable in the application.yml
 *          : file. The size is 30 and may change based on the client requirement.
 *          : Hacker news webpage is displaying only 28 in the main web page.
**************************************************************************/

@Validated
@RestController
@RequestMapping("/api/news")
@CacheConfig(cacheNames = "anato")
public class NewsPostController
{
    @Value("${app.top-posts-page-size}")
    private int topPostsPageSize;

    private NewsPostService newsService;

    public NewsPostController(NewsPostService newsService)
    {
        this.newsService = newsService;
    }

    /**************************************************************************
     * @description : Save a new new post endpoint.
    **************************************************************************/
    @PostMapping("")
    public ResponseEntity<NewsPostResponseDTO> saveNewsPost
    (@Validated @RequestBody NewsPostRequestDTO newsPost)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.saveNewPost(newsPost), HttpStatus.CREATED));
    }
    
    /**************************************************************************
     * @description : Retrieve a news post by its ID endpoint.
    **************************************************************************/
    @GetMapping("/{postId}")
    @Cacheable(cacheNames = "anato", key = "#postId")
    public ResponseEntity<NewsPostResponseDTO> getPostById
    (@Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.getPostById(postId), HttpStatus.FOUND));
    }

    /**************************************************************************
     * @description : Update a post by its ID endpoint.
    **************************************************************************/
    @PutMapping("/{postId}")
    @CacheEvict(cacheNames = "anato", key = "#postId")
    public ResponseEntity<NewsPostResponseDTO> updatePost
    (@Validated @RequestBody NewsPostRequestDTO updatedPost, @Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.updatePost(updatedPost, postId), HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Delete a post by its ID endpoint.
    **************************************************************************/
    @DeleteMapping("/{postId}")
    @CacheEvict(cacheNames = "anato", key = "#postId")
    public ResponseEntity<String> deletePost
    (@Validated @PathVariable("postId") Long postId)
    {
        newsService.deletePost(postId);
        return (new ResponseEntity<String>("Post deleted", HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Change a post by its ID endpoint.
    **************************************************************************/
    @PatchMapping("/{postId}")
    @CachePut(cacheNames = "anato", key = "#postId")
    public ResponseEntity<NewsPostResponseDTO> changePost
    (@Validated @RequestBody NewsUpdateRequestDTO changedPost, @Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.changePost(changedPost, postId), HttpStatus.OK));
    }

    /**************************************************************************
     * @description    : Retrieve all news posts endpoint.
     * @param pageable : The pagination information.
     * @return         : ResponseEntity with a Page of news posts.
    **************************************************************************/
    @GetMapping("")
    public ResponseEntity<List<NewsPostResponseDTO>> getAllPosts()
    {
        List<NewsPostResponseDTO> newsPosts = newsService.getAllPosts();
        return (new ResponseEntity<List<NewsPostResponseDTO>>(newsPosts, HttpStatus.OK));
    }
    
    /**************************************************************************
     * @description    : Retrieve the top news posts by rank endpoint;
     * @param pageable : The pagination information.
     * @return         : ResponseEntity with a Page of the top news posts.
     **************************************************************************/
    @GetMapping("/top-posts")
    @Cacheable(cacheNames = "anato", key = "top-posts")
    public ResponseEntity<Page<NewsPostResponseDTO>> getTopPostsByRank(Pageable pageable)
    {
        pageable = PageRequest.of(pageable.getPageNumber(), topPostsPageSize);
        Page<NewsPostResponseDTO> newsPosts = newsService.getPostsByRankDesc(pageable);
        return (new ResponseEntity<>(newsPosts, HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Upvote a post by its ID endpoint.
    **************************************************************************/
    @PatchMapping("/{postId}/upvote")
    @CacheEvict(cacheNames = "anato", key = "#postId")
    public ResponseEntity<NewsPostResponseDTO> upvotePost
    (@Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.upvotePost(postId), HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Downvote a post by its ID endpoint.
    **************************************************************************/
    @PatchMapping("/{postId}/downvote")
    @CacheEvict(cacheNames = "anato", key = "#postId")
    public ResponseEntity<NewsPostResponseDTO> downvotePost
    (@Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.downvotePost(postId), HttpStatus.OK));
    }
}

// It looks like you are facing a caching issue with your Spring Boot REST API when updating and deleting posts. You want to ensure that when a post is updated or deleted, the corresponding cache entries are invalidated so that the hot-posts are always up to date. However, you are facing difficulties because the hot-posts are returned as a page, and it's challenging to determine which entities to remove from the cache.

// Here are some suggestions to address this caching problem:

// 1. **Custom Cache Management**: Consider implementing custom cache management for your hot-posts. Instead of relying solely on Spring's `@Cacheable`, `@CacheEvict`, and `@CachePut` annotations, you can use a more manual approach to manage the cache yourself.

//     - When a new post is created or an existing post is updated, explicitly invalidate the hot-posts cache. You can do this by either manually maintaining a list of cache keys or using a custom cache management service.

//     - When a post is deleted, invalidate the cache in the same way as when creating or updating a post.

// 2. **Cache Entry Composition**: Instead of caching the entire page of hot-posts, consider caching the individual post entities along with their rank. This way, when a post is updated or deleted, you can easily locate and invalidate the cache entry for that specific post based on its ID.

//     - When you update a post, invalidate the cache entry for that post's ID.

//     - When you delete a post, invalidate the cache entry for that post's ID.

// 3. **Use Cacheable Composition**: You can compose your cache keys intelligently to include information about the type of request (e.g., `top-posts`) and the page parameters. This way, you can have more fine-grained control over cache eviction.

//     - For example, you can include the request type and page number in the cache key, such as `"top-posts-page-" + pageNumber`. When a post is updated or deleted, you can iterate over the cache keys that match the same request type and evict those entries.

// 4. **Scheduled Cache Eviction**: Implement a scheduled task that periodically clears the entire hot-posts cache. This can be done at regular intervals, such as every few minutes or hours, depending on how frequently your data changes. While this approach is less granular, it ensures that the cache eventually reflects the latest data.

// 5. **Consider Distributed Caching**: If your application has high data update frequency and maintaining cache consistency is critical, consider using a distributed caching solution like Redis. Redis provides more advanced caching capabilities and allows you to handle cache evictions and updates with greater control.

// Remember to thoroughly test any changes you make to your caching strategy to ensure that they work correctly and meet your performance requirements. The choice between these options will depend on your specific use case and the trade-offs you are willing to make between cache granularity and cache management complexity.