package com.akamai.MiniHackerNews.service.impl;

import java.util.List;

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

    @Override
    public NewsPost updatePost(NewsPost newPost, Long post_id)
    {
        NewsPost existingPost = getPostById(post_id);

        existingPost.setText(newPost.getText());
        existingPost.setTitle(newPost.getTitle());
        existingPost.setUpVotes(newPost.getUpVotes());
        existingPost.setDownVotes(newPost.getDownVotes());
        existingPost.setUpdatedTime(newPost.getUpdatedTime());
        existingPost.setCreationTime(newPost.getCreationTime());

        return (existingPost);
    }

    @Override
    public void deletePost(Long post_id)
    {
        getPostById(post_id);
        newsPostRepository.deleteById(post_id);
    }
}
