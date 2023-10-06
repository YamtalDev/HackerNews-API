package com.akamai.MiniHackerNews.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

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
@CacheConfig(cacheNames = "MyCache")
public class NewsPostController
{
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
    //@Cacheable(cacheNames = "MyCache", key = "#postId")
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
    //@CachePut(cacheNames = "MyCache", key = "#postId")
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
    //@CacheEvict(cacheNames = "MyCache", key = "#postId")
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
    //@CachePut(cacheNames = "MyCache", key = "#postId")
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
     * Retrieve the top news posts by rank endpoint.
     * @return ResponseEntity with a list of the top news posts.
     **************************************************************************/
    @GetMapping("/top-posts")
    //@Cacheable(cacheNames = "MyCache", key = "'top-posts'")
    public ResponseEntity<List<NewsPostResponseDTO>> getTopPostsByRank()
    {
        List<NewsPostResponseDTO> newsPosts = newsService.getPostsByRankDesc();
        return (new ResponseEntity<List<NewsPostResponseDTO>>(newsPosts, HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Upvote a post by its ID endpoint.
    **************************************************************************/
    @PatchMapping("/{postId}/upvote")
    //@CachePut(cacheNames = "MyCache", key = "#postId")
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
    //@CachePut(cacheNames = "MyCache", key = "#postId")
    public ResponseEntity<NewsPostResponseDTO> downvotePost
    (@Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.downvotePost(postId), HttpStatus.OK));
    }
}