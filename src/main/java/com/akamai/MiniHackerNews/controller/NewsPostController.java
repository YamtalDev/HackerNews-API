package com.akamai.MiniHackerNews.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import com.akamai.MiniHackerNews.schema.NewsPost;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;
import com.akamai.MiniHackerNews.dto.NewsPostRequestDTO;
import com.akamai.MiniHackerNews.service.NewsPostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

// @CrossOrigin
// Use it if you want to set up front end that fetches the server

@RestController
@RequestMapping("/api")
@CacheConfig(cacheNames = "anato")
public class NewsPostController
{
    private NewsPostService newsService;

    public NewsPostController(NewsPostService newsService)
    {
        this.newsService = newsService;
    }
    
    @PostMapping("/news")
    public ResponseEntity<NewsPost> saveNewsPost
    (@Valid @RequestBody NewsPostRequestDTO newsPost)
    {
        return (new ResponseEntity<NewsPost>
        (newsService.saveNewsPost(newsPost), HttpStatus.CREATED));
    }

    @GetMapping("/news")
    public ResponseEntity<Page<NewsPost>> getAllPosts(Pageable pageable)
    {
        Page<NewsPost> newsPosts = newsService.getAllPosts(pageable);
        return (new ResponseEntity<Page<NewsPost>>(newsPosts, HttpStatus.OK));
    }

    @Cacheable(key = "#post_id")
    @GetMapping("/news/{post_id}")
    public ResponseEntity<NewsPost> getPostById
    (@Valid @PathVariable("post_id") Long post_id)
    {
        return (new ResponseEntity<NewsPost>
        (newsService.getPostById(post_id), HttpStatus.OK));
    }

    @CachePut(key = "#post_id")
    @PutMapping("/news/{post_id}")
    public ResponseEntity<NewsPost> updatePost
    (@Valid @RequestBody NewsPostRequestDTO updatedPost, @Valid @PathVariable("post_id") Long post_id)
    {
        return (new ResponseEntity<NewsPost>
        (newsService.updatePost(updatedPost, post_id), HttpStatus.OK));
    }

    @CacheEvict(key = "#post_id")
    @DeleteMapping("/news/{post_id}")
    public ResponseEntity<String> deletePost
    (@Valid @PathVariable("post_id") Long post_id)
    {
        newsService.deletePost(post_id);
        return (new ResponseEntity<String>("Post deleted", HttpStatus.OK));
    }

    @PatchMapping("/news/{post_id}/upvote")
    public ResponseEntity<NewsPost> upvotePost
    (@Valid @PathVariable("post_id") Long post_id)
    {
        return (new ResponseEntity<NewsPost>
        (newsService.upvotePost(post_id), HttpStatus.OK));
    }

    @PatchMapping("/news/{post_id}/downvote")
    public ResponseEntity<NewsPost> downvotePost
    (@Valid @PathVariable("post_id") Long post_id)
    {
        return (new ResponseEntity<NewsPost>
        (newsService.downvotePost(post_id), HttpStatus.OK));
    }

    @Cacheable("anato")
    @GetMapping("/news/top-posts")
    public ResponseEntity<Page<NewsPost>> getTopPostsByRank(Pageable pageable) {
        Page<NewsPost> newsPosts = newsService.getPostsByRankDesc(pageable);
        return new ResponseEntity<>(newsPosts, HttpStatus.OK);
    }
}
