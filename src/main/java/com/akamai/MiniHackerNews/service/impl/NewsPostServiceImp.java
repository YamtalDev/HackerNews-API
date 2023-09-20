package com.akamai.MiniHackerNews.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.akamai.MiniHackerNews.exception.ResourceNotFoundException;
import com.akamai.MiniHackerNews.model.NewsPost;
import com.akamai.MiniHackerNews.repository.NewsPostRepository;
import com.akamai.MiniHackerNews.service.NewsPostService;

@Service
public class NewsPostServiceImp implements NewsPostService
{
    private NewsPostRepository newsPostRepository;

    public NewsPostServiceImp(NewsPostRepository newsPostRepository)
    {
        super();
        this.newsPostRepository = newsPostRepository;
    }

    @Override
    public NewsPost saveNewsPost(NewsPost newsPost)
    {
        return (newsPostRepository.save(newsPost));
    }

    @Override
    public List<NewsPost> getAllPosts()
    {
        return (newsPostRepository.findAll());
    }

    @Override
    public NewsPost getPostById(Long post_id)
    {
        return (newsPostRepository.findById(post_id).orElseThrow(() -> new ResourceNotFoundException("post_id", post_id, "post")));
    }
}
