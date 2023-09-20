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

        existingPost.setLink(newPost.getLink());
        existingPost.setTitle(newPost.getTitle());
        existingPost.setVotes(newPost.getVotes());
        existingPost.setUserName(newPost.getUserName());
        existingPost.setCreationTime(newPost.getCreationTime());

        return (existingPost);
    }

    @Override
    public void deletePost(Long post_id)
    {
        getPostById(post_id);
        newsPostRepository.deleteById(post_id);
    }

    public NewsPost upvotePost(Long post_id)
    {
        NewsPost post = getPostById(post_id);
        post.setVotes(post.getVotes() + 1);
        return updatePost(post, post_id);
    }

    public NewsPost downvotePost(Long post_id)
    {
        NewsPost post = getPostById(post_id);
        post.setVotes(post.getVotes() - 1);
        return updatePost(post, post_id);
    }
}
