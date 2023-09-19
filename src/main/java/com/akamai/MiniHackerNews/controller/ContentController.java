package com.akamai.MiniHackerNews.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.akamai.MiniHackerNews.model.Content;
import com.akamai.MiniHackerNews.repository.ContentCollectionRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/content")
public class ContentController
{
    private final ContentCollectionRepository repository;

    public ContentController(ContentCollectionRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("")
    public List<Content> findAll()
    {
        return (repository.findAll());
    }

    @GetMapping("/{id}")
    public Content findById(@PathVariable Integer id)
    {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found"));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Content content)
    {
        repository.save(content);
    }

    // Create a costume exception my content not found exception
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Content content, @PathVariable Integer id)
    {
        if(repository.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found");
        }

        repository.save(content);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id)
    {

        repository.delete(id);
    }
}
