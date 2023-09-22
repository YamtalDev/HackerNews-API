package com.akamai.MiniHackerNews.service.impl;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.akamai.MiniHackerNews.schema.*;

import com.akamai.MiniHackerNews.repository.*;
import com.akamai.MiniHackerNews.schema.dto.*;
import com.akamai.MiniHackerNews.service.NewsPostService;
import com.akamai.MiniHackerNews.exception.ValidationException;
import com.akamai.MiniHackerNews.exception.NewsPostNotFoundException;

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

    public NewsPostServiceImpl(ModelMapper modelMapper, NewsPostRepository newsPostRepository)
    {
        this.modelMapper = modelMapper;
        this.newsPostRepository = newsPostRepository;
    }

    @Override
    public NewsPostResponseDTO saveNewsPost(NewsPostRequestDTO newsPostDTO) throws ValidationException
    {
        try
        {
            NewsPostSchema newPost = new NewsPostSchema();
            newPost.setLink(newsPostDTO.getLink());
            newPost.setPost(newsPostDTO.getPost());
            newPost.setPostedBy(newsPostDTO.getPostedBy());
            return (modelMapper.map(newsPostRepository.save(newPost), NewsPostResponseDTO.class));
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
    public NewsPostResponseDTO getPostById(Long post_id) throws NewsPostNotFoundException
    {
        return (modelMapper.map(getPostEntityById(post_id),NewsPostResponseDTO.class));
    }

    @Override
    public Page<NewsPostResponseDTO> getAllPosts(Pageable pageable)
    {
        return (newsPostRepository.findAll(pageable)).map
        (entity -> modelMapper.map(entity, NewsPostResponseDTO.class));
    }

    @Override
    public Page<NewsPostResponseDTO> getPostsByRankDesc(Pageable pageable)
    {
        return (newsPostRepository.findByOrderByRankDesc(pageable).map
        (entity -> modelMapper.map(entity, NewsPostResponseDTO.class)));
    }

    @Override
    public NewsPostResponseDTO updatePost(NewsPostRequestDTO newsPostDTO, Long post_id) throws ValidationException
    {
        try
        {
            NewsPostSchema existingPost = getPostEntityById(post_id);

            existingPost.setCreationTime();
            existingPost.setLink(newsPostDTO.getLink());
            existingPost.setPost(newsPostDTO.getPost());
            existingPost.setPostedBy(newsPostDTO.getPostedBy());
            return (modelMapper.map(newsPostRepository.save(existingPost), NewsPostResponseDTO.class));
        }
        catch (DataIntegrityViolationException |  NewsPostNotFoundException exception)
        {
            throw (new ValidationException("Invalid input data."));
        }
    }

    @Override
    public NewsPostResponseDTO changePost(NewsUpdateRequestDTO newsPostDTO, Long post_id) throws ValidationException
    {
        try
        {
            NewsPostSchema existingPost = getPostEntityById(post_id);

            existingPost.setLink(newsPostDTO.getLink());
            existingPost.setPost(newsPostDTO.getPost());
            return (modelMapper.map(newsPostRepository.save(existingPost), NewsPostResponseDTO.class));
        }
        catch (DataIntegrityViolationException |  NewsPostNotFoundException exception)
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
            NewsPostSchema existingPost = getPostEntityById(post_id);
            existingPost.upVote();
            return (newsPostRepository.save(existingPost).getVotes());
        }
        catch(DataIntegrityViolationException | NewsPostNotFoundException exception)
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
            NewsPostSchema existingPost = getPostEntityById(post_id);
            existingPost.downVote();
            return (newsPostRepository.save(existingPost).getVotes());
        }
        catch(DataIntegrityViolationException | NewsPostNotFoundException exception)
        {
            throw (new ValidationException("Cannot downvote below zero."));
        }
    }

    private NewsPostSchema getPostEntityById(Long post_id) throws NewsPostNotFoundException
    {
        return (newsPostRepository.findById(post_id).orElseThrow(() -> 
        new NewsPostNotFoundException("post_id", post_id, "post")));
    }
}
