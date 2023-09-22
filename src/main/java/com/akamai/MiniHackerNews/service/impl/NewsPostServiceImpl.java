package com.akamai.MiniHackerNews.service.impl;

import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.akamai.MiniHackerNews.schema.NewsPostEntity;
import com.akamai.MiniHackerNews.schema.dto.NewsPostRequest;
import com.akamai.MiniHackerNews.schema.dto.NewsPostResponse;
import com.akamai.MiniHackerNews.service.NewsPostService;
import com.akamai.MiniHackerNews.exception.ResourceNotFoundException;
import com.akamai.MiniHackerNews.exception.ValidationException;
import com.akamai.MiniHackerNews.repository.NewsPostRepository;

import org.springframework.dao.DataIntegrityViolationException;

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
    private ModelMapper modelMapper;
    private NewsPostRepository newsPostRepository;

    public NewsPostServiceImpl(NewsPostRepository newsPostRepository, ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
        this.newsPostRepository = newsPostRepository;
    }
    
    @Override
    public NewsPostResponse saveNewsPost(NewsPostRequest newsPostDTO) throws ValidationException
    {
        NewsPostEntity newsPost = new NewsPostEntity();
        newsPost.setLink(newsPostDTO.getLink());
        newsPost.setPost(newsPostDTO.getPost());
        newsPost.setUserName(newsPostDTO.getUserName());
        
        try
        {
            return (modelMapper.map
            (newsPostRepository.save(newsPost), NewsPostResponse.class));
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

    @Override
    public NewsPostResponse getPostById(Long post_id) throws ResourceNotFoundException
    {
        return (modelMapper.map(getPostEntityById(post_id),NewsPostResponse.class));
    }

    @Override
    public Page<NewsPostResponse> getAllPosts(Pageable pageable)
    {
        return (newsPostRepository.findAll(pageable)).map
        (entity -> modelMapper.map(entity, NewsPostResponse.class));
    }
    
    @Override
    public Page<NewsPostResponse> getPostsByRankDesc(Pageable pageable)
    {
        return (newsPostRepository.findByOrderByRankDesc(pageable).map
        (entity -> modelMapper.map(entity, NewsPostResponse.class)));
    }

    @Override
    public NewsPostResponse updatePost(NewsPostRequest newsPostDTO, Long post_id) throws ValidationException
    {
        try
        {
            NewsPostEntity existingPost = getPostEntityById(post_id);
            existingPost.setLink(newsPostDTO.getLink());
            existingPost.setPost(newsPostDTO.getPost());
            existingPost.setUserName(newsPostDTO.getUserName());
            return (modelMapper.map(newsPostRepository.save(existingPost), NewsPostResponse.class));
        }
        catch (DataIntegrityViolationException |  ResourceNotFoundException exception)
        {
            throw (new ValidationException("Invalid input data."));
        }
    }

    /*************************************************************************
    * It can be a better approach to implement a specific query to the data base 
    * that will increment and decrement an entity vote directly without fetching the entity.
    *************************************************************************/
    @Override
    public int upvotePost(Long post_id) throws ValidationException
    {
        try
        {
            NewsPostEntity existingPost = getPostEntityById(post_id);
            existingPost.setVotes(existingPost.getVotes() + 1);
            newsPostRepository.save(existingPost);
            return (existingPost.getVotes());
        }
        catch(DataIntegrityViolationException | ResourceNotFoundException exception)
        {
            throw (new ValidationException("Maximum votes reached."));
        }
    }

    /*************************************************************************
    * It can be a better approach to implement a specific query to the data base 
    * that will increment and decrement an entity vote directly without fetching the entity.
    *************************************************************************/    @Override
    public int downvotePost(Long post_id) throws ValidationException
    {
        try
        {
            NewsPostEntity existingPost = getPostEntityById(post_id);
            existingPost.setVotes(existingPost.getVotes() - 1);
            newsPostRepository.save(existingPost);
            return (existingPost.getVotes());
        }
        catch(DataIntegrityViolationException | ResourceNotFoundException exception)
        {
            throw (new ValidationException("Cannot downvote below zero."));
        }
    }

    public NewsPostEntity getPostEntityById(Long post_id) throws ResourceNotFoundException
    {
        return (newsPostRepository.findById(post_id).orElseThrow(() -> 
        new ResourceNotFoundException("post_id", post_id, "post")));
    }
}
