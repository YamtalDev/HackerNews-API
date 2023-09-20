package com.akamai.MiniHackerNews.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akamai.MiniHackerNews.dto.NewsPostUserRequestDTO;
import com.akamai.MiniHackerNews.schema.NewsPost;
import com.akamai.MiniHackerNews.service.NewsPostService;

import jakarta.validation.Valid;

// @CrossOrigin
// Use it if you want to set up front end that fetches the server
@RestController
@RequestMapping("/api")
public class NewsPostController
{
    private NewsPostService newsService;

    public NewsPostController(NewsPostService newsService)
    {
        this.newsService = newsService;
    }
    
    @PostMapping("/news")
    public ResponseEntity<NewsPost> saveNewsPost
    (@Valid @RequestBody NewsPostUserRequestDTO newsPost)
    {
        return (new ResponseEntity<NewsPost>
        (newsService.saveNewsPost(newsPost), HttpStatus.CREATED));
    }

    @GetMapping("/news")
    public List<NewsPost> getAllPosts()
    {
        return (newsService.getAllPosts());
    }

    @GetMapping("/news/{post_id}")
    public ResponseEntity<NewsPost> getPostById
    (@PathVariable("post_id") Long post_id)
    {
        return (new ResponseEntity<NewsPost>
        (newsService.getPostById(post_id), HttpStatus.OK));
    }

    @PutMapping("/news/{post_id}")
    public ResponseEntity<NewsPost> updatePost
    (@Valid @RequestBody NewsPostUserRequestDTO updatedPost, @PathVariable("post_id") Long post_id)
    {
        return (new ResponseEntity<NewsPost>
        (newsService.updatePost(updatedPost, post_id), HttpStatus.OK));
    }

    @DeleteMapping("/news/{post_id}")
    public ResponseEntity<String> deletePost
    (@PathVariable("post_id") Long post_id)
    {
        newsService.deletePost(post_id);
        return (new ResponseEntity<String>("Post deleted", HttpStatus.OK));
    }

    @PutMapping("/news/{post_id}/upvote")
    public ResponseEntity<NewsPost> upvotePost
    (@PathVariable("post_id") Long post_id)
    {
        return (new ResponseEntity<NewsPost>
        (newsService.upvotePost(post_id), HttpStatus.OK));
    }

    @PutMapping("/news/{post_id}/downvote")
    public ResponseEntity<NewsPost> downvotePost
    (@PathVariable("post_id") Long post_id)
    {
        return (new ResponseEntity<NewsPost>
        (newsService.downvotePost(post_id), HttpStatus.OK));
    }
}
