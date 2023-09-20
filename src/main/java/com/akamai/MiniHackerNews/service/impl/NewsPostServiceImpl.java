package com.akamai.MiniHackerNews.service.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.akamai.MiniHackerNews.schema.NewsPost;
import com.akamai.MiniHackerNews.service.NewsPostService;
import com.akamai.MiniHackerNews.dto.NewsPostUserRequestDTO;
import com.akamai.MiniHackerNews.exception.ResourceNotFoundException;
import com.akamai.MiniHackerNews.exception.ValidationException;
import com.akamai.MiniHackerNews.repository.NewsPostRepository;

/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
 * @description:  
******************************************************************************/

@Service
public class NewsPostServiceImpl implements NewsPostService
{
    private NewsPostRepository newsPostRepository;

    public NewsPostServiceImpl(NewsPostRepository newsPostRepository)
    {
        this.newsPostRepository = newsPostRepository;
    }

    @Override
    public NewsPost saveNewsPost(NewsPostUserRequestDTO newsPostDTO)
    {
        NewsPost newsPost = new NewsPost();
        newsPost.setLink(newsPostDTO.getLink());
        newsPost.setTitle(newsPostDTO.getTitle());
        newsPost.setUserName(newsPostDTO.getUserName());

        try
        {
            return (newsPostRepository.save(newsPost));
        }
        catch (DataIntegrityViolationException exception)
        {
            throw (new ValidationException("Invalid input data."));
        }
    }

    @Override
    public Page<NewsPost> getAllPosts(Pageable pageable)
    {
        return (newsPostRepository.findAll(pageable));
    }

    @Override
    public NewsPost getPostById(Long post_id)
    {
        return (newsPostRepository.findById(post_id).orElseThrow(() -> new ResourceNotFoundException("post_id", post_id, "post")));
    }

    @Override
    public NewsPost updatePost(NewsPostUserRequestDTO newsPostDTO, Long post_id)
    {
        NewsPost existingPost = getPostById(post_id);
        existingPost.setLink(newsPostDTO.getLink());
        existingPost.setTitle(newsPostDTO.getTitle());
        existingPost.setUserName(newsPostDTO.getUserName());

        try
        {
            return (newsPostRepository.save(existingPost));
        }
        catch (DataIntegrityViolationException exception)
        {
            throw (new ValidationException("Invalid input data."));
        }
    }

    @Override
    public void deletePost(Long post_id)
    {
        getPostById(post_id);
        newsPostRepository.deleteById(post_id);
    }

    public NewsPost upvotePost(Long post_id)
    {
        NewsPost existingPost = getPostById(post_id);
        existingPost.setVotes(existingPost.getVotes() + 1);

        try
        {
            return (newsPostRepository.save(existingPost));
        }
        catch(DataIntegrityViolationException exception)
        {
            throw new ValidationException("Maximum votes reached.");
        }
    }

    public NewsPost downvotePost(Long post_id)
    {
        NewsPost existingPost = getPostById(post_id);
        existingPost.setVotes(existingPost.getVotes() - 1);

        try
        {
            return (newsPostRepository.save(existingPost));
        }
        catch(DataIntegrityViolationException exception)
        {
            throw new ValidationException("Cannot downvote below zero.");
        }
    }
}
