package com.akamai.MiniHackerNews.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akamai.MiniHackerNews.model.NewsPost;
import com.akamai.MiniHackerNews.service.NewsPostService;

import jakarta.validation.Valid;

// @CrossOrigin
// use it only if you want to set up front end that fetches the server
@RestController
@RequestMapping("/api/news")
public class NewsPostController
{
    private NewsPostService newsService;

    public NewsPostController(NewsPostService newsService)
    {
        this.newsService = newsService;
    }
    
    @PostMapping("")
    public ResponseEntity<NewsPost> saveNewsPost(@Valid @RequestBody NewsPost newsPost)
    {
        return (new ResponseEntity<NewsPost>(newsService.saveNewsPost(newsPost), HttpStatus.CREATED));
    }

    @GetMapping("")
    public List<NewsPost> getAllPosts()
    {
        return (newsService.getAllPosts());
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<NewsPost> getPostById(@PathVariable("post_id") Long post_id)
    {
        return (new ResponseEntity<NewsPost>(newsService.getPostById(post_id), HttpStatus.OK));
    }
}
