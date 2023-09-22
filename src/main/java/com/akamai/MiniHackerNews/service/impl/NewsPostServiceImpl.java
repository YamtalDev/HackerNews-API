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
    public NewsPostResponseDTO saveNewPost(NewsPostRequestDTO newsPostDTO) throws ValidationException
    {
        NewsPostSchema newPost = new NewsPostSchema();
        newPost.setPostedBy(newsPostDTO.getPostedBy());
        newPost.setPost(newsPostDTO.getPost());
        newPost.setLink(newsPostDTO.getLink());

        try
        {
            return (modelMapper.map(newsPostRepository.save(newPost), NewsPostResponseDTO.class));
        }
        catch (DataIntegrityViolationException exception)
        {
            throw (new ValidationException("Invalid input data."));
        }
    }

    @Override
    public void deletePost(Long postId)
    {
        getPostById(postId);
        newsPostRepository.deleteById(postId);
    }

    @Override
    public NewsPostResponseDTO getPostById(Long postId) throws NewsPostNotFoundException
    {
        return (modelMapper.map(getPostEntityById(postId),NewsPostResponseDTO.class));
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
    public NewsPostResponseDTO updatePost(NewsPostRequestDTO newsPostDTO, Long postId) throws ValidationException
    {
        try
        {
            NewsPostSchema existingPost = getPostEntityById(postId);

            existingPost.setTime();
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
    public NewsPostResponseDTO changePost(NewsUpdateRequestDTO newsPostDTO, Long postId) throws ValidationException
    {
        try
        {
            NewsPostSchema existingPost = getPostEntityById(postId);

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
    public int upvotePost(Long postId) throws ValidationException
    {
        try
        {
            NewsPostSchema existingPost = getPostEntityById(postId);
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
    public int downvotePost(Long postId) throws ValidationException
    {
        try
        {
            NewsPostSchema existingPost = getPostEntityById(postId);
            existingPost.downVote();
            return (newsPostRepository.save(existingPost).getVotes());
        }
        catch(DataIntegrityViolationException | NewsPostNotFoundException exception)
        {
            throw (new ValidationException("Cannot downvote below zero."));
        }
    }

    private NewsPostSchema getPostEntityById(Long postId) throws NewsPostNotFoundException
    {
        return (newsPostRepository.findById(postId).orElseThrow(() -> 
        new NewsPostNotFoundException("postId", postId, "post")));
    }
}
