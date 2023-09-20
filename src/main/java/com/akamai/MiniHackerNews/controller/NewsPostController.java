package com.akamai.MiniHackerNews.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // @GetMapping("")
    // public List<NewsPost> findAll()
    // {
    //     return (repository.findAll());
    // }

    // @GetMapping("/{id}")
    // public NewsPost findById(@PathVariable Integer id)
    // {
    //     return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found"));
    // }

    // @PostMapping("")
    // @ResponseStatus(HttpStatus.CREATED)
    // public void create(@Valid @RequestBody NewsPost content)
    // {
    //     repository.save(content);
    // }

    // // Create a costume exception my content not found exception
    // @PutMapping("/{id}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void update(@Valid @RequestBody NewsPost content, @PathVariable Integer id)
    // {
    //     if(repository.existsById(id))
    //     {
    //         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found");
    //     }

    //     repository.save(content);
    // }


    // @DeleteMapping("/{id}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void delete(@PathVariable Integer id)
    // {

    //     repository.delete(id);
    // }
}
