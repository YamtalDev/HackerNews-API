/******************************************************************************
MIT License

Copyright (c) 2023 Tal Aharon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

******************************************************************************/
package com.akamai.MiniHackerNews.service.impl;

import org.modelmapper.ModelMapper;
import com.akamai.MiniHackerNews.schema.*;
import org.springframework.data.domain.Page;
import com.akamai.MiniHackerNews.repository.*;
import com.akamai.MiniHackerNews.schema.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.akamai.MiniHackerNews.service.NewsPostService;
import org.springframework.dao.DataIntegrityViolationException;
import com.akamai.MiniHackerNews.exception.ValidationException;
import com.akamai.MiniHackerNews.exception.NewsPostNotFoundException;

@Service
public class NewsPostServiceImpl implements NewsPostService
{
    private ModelMapper modelMapper;
    private NewsPostRepository newsPostRepository;

    public NewsPostServiceImpl
    (ModelMapper modelMapper, NewsPostRepository newsPostRepository)
    {
        this.modelMapper = modelMapper;
        this.newsPostRepository = newsPostRepository;
    }

    @Override
    public NewsPostResponseDTO saveNewPost(NewsPostRequestDTO newsPostDTO) throws ValidationException
    {
        try
        {
            NewsPostSchema newPost = modelMapper.map(newsPostDTO, NewsPostSchema.class);
            return (modelMapper.map(newsPostRepository.save(newPost), NewsPostResponseDTO.class));
        }
        catch(DataIntegrityViolationException exception)
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
            modelMapper.map(newsPostDTO, existingPost);
            return (modelMapper.map(newsPostRepository.save(existingPost), NewsPostResponseDTO.class));
        }
        catch(DataIntegrityViolationException |  NewsPostNotFoundException exception)
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
            modelMapper.map(newsPostDTO, existingPost);
            return (modelMapper.map(newsPostRepository.save(existingPost), NewsPostResponseDTO.class));
        }
        catch(DataIntegrityViolationException |  NewsPostNotFoundException exception)
        {
            throw (new ValidationException("Invalid input data."));
        }
    }

    /*************************************************************************
    * It can be a better approach to implement a specific query to the data base 
    * that will increment and decrement an entity vote directly without fetching the entity.
    *************************************************************************/
    @Override
    public NewsPostResponseDTO upvotePost(Long postId) throws ValidationException
    {
        NewsPostSchema existingPost = getPostEntityById(postId);
        if(Integer.MAX_VALUE == existingPost.getVotes())
        {
            throw (new ValidationException("Maximum votes reached."));
        }

        existingPost.upVote();
        return (modelMapper.map(newsPostRepository.save(existingPost), NewsPostResponseDTO.class));
    }

    /*************************************************************************
    * It can be a better approach to implement a specific query to the data base 
    * that will increment and decrement an entity vote directly without fetching the entity.
    *************************************************************************/
    @Override
    public NewsPostResponseDTO downvotePost(Long postId) throws ValidationException
    {
        NewsPostSchema existingPost = getPostEntityById(postId);
        if(existingPost.getVotes() == 0)
        {
            throw new ValidationException("Cannot downvote below zero.");
        }

        existingPost.downVote();
        return (modelMapper.map(newsPostRepository.save(existingPost), NewsPostResponseDTO.class));
    }

    private NewsPostSchema getPostEntityById(Long postId) throws NewsPostNotFoundException
    {
        return (newsPostRepository.findById(postId).orElseThrow(() -> 
        new NewsPostNotFoundException("postId", postId, "post")));
    }
}
