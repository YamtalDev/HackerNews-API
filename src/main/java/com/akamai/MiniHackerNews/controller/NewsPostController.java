package com.akamai.MiniHackerNews.controller;

import jakarta.validation.Valid;

import com.akamai.MiniHackerNews.service.*;
import com.akamai.MiniHackerNews.schema.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
 * @description : 

 ******************************************************************************/
@RestController
@RequestMapping("/api/news")
@CacheConfig(cacheNames = "anato")
public class NewsPostController
{
    private NewsPostService newsService;

    public NewsPostController(NewsPostService newsService)
    {
        this.newsService = newsService;
    }
    
    @PostMapping(value = "")
    public ResponseEntity<NewsPostResponseDTO> saveNewsPost
    (@Valid @RequestBody NewsPostRequestDTO newsPost)
    {
        return new ResponseEntity<NewsPostResponseDTO>
        (newsService.saveNewsPost(newsPost), HttpStatus.CREATED);
    }

    @GetMapping(value = "")
    public ResponseEntity<Page<NewsPostResponseDTO>> getAllPosts(Pageable pageable)
    {
        Page<NewsPostResponseDTO> newsPosts = newsService.getAllPosts(pageable);
        return (new ResponseEntity<Page<NewsPostResponseDTO>>(newsPosts, HttpStatus.OK));
    }

    @Cacheable("anato")
    @GetMapping(value = "/top-posts")
    public ResponseEntity<Page<NewsPostResponseDTO>> getTopPostsByRank(Pageable pageable)
    {
        Page<NewsPostResponseDTO> newsPosts = newsService.getPostsByRankDesc(pageable);
        return (new ResponseEntity<Page<NewsPostResponseDTO>>(newsPosts, HttpStatus.OK));
    }

    @Cacheable(key = "#post_id")
    @GetMapping(value = "/{postId}")
    public ResponseEntity<NewsPostResponseDTO> getPostById
    (@Valid @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.getPostById(postId), HttpStatus.OK));
    }

    @CachePut(key = "#post_id")
    @PutMapping(value = "/{postId}")
    public ResponseEntity<NewsPostResponseDTO> updatePost
    (@Valid @RequestBody NewsPostRequestDTO updatedPost, @Valid @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.updatePost(updatedPost, postId), HttpStatus.OK));
    }

    @CacheEvict(key = "#post_id")
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<String> deletePost
    (@Valid @PathVariable("postId") Long postId)
    {
        newsService.deletePost(postId);
        return (new ResponseEntity<String>("Post deleted", HttpStatus.OK));
    }

    @PatchMapping(value = "/{post_id}")
    public ResponseEntity<NewsPostResponseDTO> changePost
    (@Valid @RequestBody NewsUpdateRequestDTO changedPost, @Valid @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.changePost(changedPost, postId), HttpStatus.OK));
    }

    @PatchMapping(value = "/{post_id}/upvote")
    public ResponseEntity<String> upvotePost
    (@Valid @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<String>
        ( "👍 " + newsService.upvotePost(postId), HttpStatus.OK));
    }

    @PatchMapping(value = "/{post_id}/downvote")
    public ResponseEntity<String> downvotePost
    (@Valid @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<String>
        ("👎 " + newsService.downvotePost(postId), HttpStatus.OK));
    }
}
