/******************************************************************************

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

package com.akamai.HackerNews.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.akamai.HackerNews.dto.*;
import com.akamai.HackerNews.exception.NewsPostNotFoundException;
import com.akamai.HackerNews.exception.ValidationException;
import com.akamai.HackerNews.repository.*;
import com.akamai.HackerNews.schema.*;
import com.akamai.HackerNews.service.NewsPostService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;

/******************************************************************************
 * @description: Implementation of the NewsPostService responsible for handling 
 * news posts database entity operations and caching. Provides functionality for the
 * controller layer. The methods in this service layer use the repository interface 
 * methods (JPA Repository) for database operations.
 * 
 * @exceptions: All exceptions thrown from the service functions are handled 
 * by the global exception manager.
******************************************************************************/
@Service
public class NewsPostServiceImpl implements NewsPostService
{
    private ModelMapper modelMapper;
    NewsPostsCacheServiceImpl cacheService;
    private NewsPostRepository newsPostRepository;

    @Value("${app.cache.top-posts-size}")
    private int maxTopPostsSize;

    private boolean topPostsCachePopulated;

    /**************************************************************************
     * @description              : Constructs a NewsPostServiceImpl instance.
     * @param modelMapper        : Injected ModelMapper instance.
     * @param cacheService       : Injected NewsPostsCacheServiceImpl instance.
     * @param newsPostRepository : Injected NewsPostRepository instance.
    **************************************************************************/
    public NewsPostServiceImpl
    (ModelMapper modelMapper, NewsPostsCacheServiceImpl cacheService, NewsPostRepository newsPostRepository)
    {
        this.topPostsCachePopulated = false;
        this.modelMapper = modelMapper;
        this.cacheService = cacheService;
        this.newsPostRepository = newsPostRepository;
    }

    /**************************************************************************
     * @description       : Create and save a new post based on the DTO object.
     * @param newsPostDTO : The DTO containing the new post data.
     * @return            : The saved news post as a mapped response DTO object.
     * @cache             : The result that is returned are being cached.
     * @throws ValidationException : If the input data is invalid.
    **************************************************************************/
    @Override
    public NewsPostResponseDTO saveNewPost
    (NewsPostRequestDTO newsPostDTO) throws ValidationException
    {
        try
        {
            NewsPostSchema newPost = modelMapper.map(newsPostDTO, NewsPostSchema.class);

            NewsPostResponseDTO responseDTO = modelMapper.map
            (newsPostRepository.save(newPost), NewsPostResponseDTO.class);

            cacheService.put(responseDTO, newPost.getRank());
            return (responseDTO);
        }
        catch(DataIntegrityViolationException exception)
        {
            throw (new ValidationException("Invalid input data."));
        }
    }

    /**************************************************************************
     * @description  : Delete a post based on its id.
     * @param postId : The ID of the post to be deleted.
     * @cache        : The result will be evicted from the cache.
     * @throws NewsPostNotFoundException : If the specified post does not exist.
    **************************************************************************/
    @Override
    public void deletePost(Long postId) throws NewsPostNotFoundException
    {
        if(!newsPostRepository.existsById(postId))
        {
            throw (new NewsPostNotFoundException("postId", postId, "post"));
        }

        newsPostRepository.deleteById(postId);
        cacheService.evict(postId);
    }

    /**************************************************************************
     * @description : Retrieve a post by its ID.
     * @return      : The retrieved post as a response DTO.
     * @cache       : The result that is returned are being cached.
     * @throws NewsPostNotFoundException : If the specified post does not exist.
    **************************************************************************/
    @Override
    public NewsPostResponseDTO getPostById
    (Long postId) throws NewsPostNotFoundException
    {
        NewsPostResponseDTO cachedPost = cacheService.get(postId);
        if(null != cachedPost)
        {
            return (cachedPost);
        }

        NewsPostSchema newsPost = getPostEntityById(postId);
        NewsPostResponseDTO responseDTO = modelMapper.map(newsPost,NewsPostResponseDTO.class);

        cacheService.put(responseDTO, newsPost.getRank());
        return (responseDTO);
    }

    /**************************************************************************
     * @description : Retrieves a paginated list of all news posts.
     * @return      : A list containing news posts as response DTOs.
    **************************************************************************/
    @Override
    public List<NewsPostResponseDTO> getAllPosts()
    {
        List<NewsPostSchema> allPosts = newsPostRepository.findAll();

        return(allPosts.stream()
        .map(newsPost -> modelMapper.map(newsPost, NewsPostResponseDTO.class))
        .collect(Collectors.toList()));
    }

    /**************************************************************************
     * @description : Retrieves the top news posts ordered by rank in descending order.
     * @cache       : The result that is returned are being cached.
     * @return      : A list of the top news posts in descending rank order.
    **************************************************************************/
    @Override
    public List<NewsPostResponseDTO> getPostsByRankDesc()
    {
        List<NewsPostResponseDTO> cachedTopPosts = cacheService.getTopPostsFromCache();
        if(!topPostsCachePopulated || cachedTopPosts.isEmpty())
        {
            List<NewsPostSchema> topPosts = newsPostRepository
            .findByOrderByRankDescWithLimit(maxTopPostsSize);

            List<NewsPostResponseDTO> topPostsDTO = topPosts.stream()
            .map(newsPost -> modelMapper.map(newsPost, NewsPostResponseDTO.class))
            .collect(Collectors.toList());

            cacheService.putTopPosts(topPosts);

            topPostsCachePopulated = true;

            return (topPostsDTO);
        }
        else
        {
            return (cachedTopPosts);
        }
    }

    /**************************************************************************
     * @description       : Updates an existing news post with new data.
     * @param newsPostDTO : The DTO containing updated post data.
     * @param postId      : The ID of the post to be updated.
     * @return            : The updated news post as a response DTO.
     * @cache             : The result returned by this method is cached.
     * @throws ValidationException : If the input data is invalid.
    **************************************************************************/
    @Override
    public NewsPostResponseDTO updatePost
    (NewsPostRequestDTO newsPostDTO, Long postId) throws ValidationException
    {
        try
        {
            NewsPostSchema existingPost = getPostEntityById(postId);

            modelMapper.map(newsPostDTO, existingPost);

            NewsPostResponseDTO responseDTO = modelMapper.map
            (newsPostRepository.save(existingPost), NewsPostResponseDTO.class);

            cacheService.put(responseDTO, existingPost.getRank());
            return (responseDTO);
        }
        catch(DataIntegrityViolationException |  NewsPostNotFoundException exception)
        {
            throw (new ValidationException("Invalid input data."));
        }
    }

    /**************************************************************************
     * @description       : Changes the data of an existing news post.
     * @param newsPostDTO : The DTO containing updated post data.
     * @param postId      : The ID of the post to be changed.
     * @return            : The changed news post as a response DTO.
     * @cache             : The result returned by this method is cached.
     * @throws ValidationException : If the input data is invalid.
    **************************************************************************/
    @Override
    public NewsPostResponseDTO changePost
    (NewsUpdateRequestDTO newsPostDTO, Long postId) throws ValidationException
    {
        try
        {
            NewsPostSchema existingPost = getPostEntityById(postId);
            modelMapper.map(newsPostDTO, existingPost);

            NewsPostResponseDTO responseDTO = modelMapper.map
            (newsPostRepository.save(existingPost), NewsPostResponseDTO.class);

            cacheService.put(responseDTO, existingPost.getRank());
            return (responseDTO);
        }
        catch(DataIntegrityViolationException |  NewsPostNotFoundException exception)
        {
            throw (new ValidationException("Invalid input data."));
        }
    }

    /**************************************************************************
     * @description  : Upvote a post by incrementing its vote count.
     *
     * @param postId : The ID of the post to upvote.
     * @return       : The upvoted post as a response DTO.
     * @cache        : The result returned by this method is cached.
     * @throws ValidationException : If the maximum votes have been reached.
     * 
     * @implNote     : It can be a better approach to implement a specific query 
     *               : to upvote or downvote directly without fetching the 
     *               : entity from the database.
     *
     * @implNote     : When a post's votes are decremented below 0 or incremented above
     *               : the max value, a low-level SQL exception arises due to the
     *               : restrictions in the schema. Currently, there is an if statement
     *               : that checks the boundaries and throws a higher-level exception
     *               : to the exception manager to handle. Otherwise, a JPA transaction
     *               : error occurs. Consider a better approach for handling this scenario.

    **************************************************************************/
    @Override
    public NewsPostResponseDTO upvotePost(Long postId) throws ValidationException
    {
        NewsPostSchema existingPost = getPostEntityById(postId);
        if(Integer.MAX_VALUE == existingPost.getVotes())
        {
            throw (new ValidationException("Maximum votes reached."));
        }

        existingPost.upVote();

        NewsPostResponseDTO responseDTO = modelMapper.map
        (newsPostRepository.save(existingPost), NewsPostResponseDTO.class);

        cacheService.put(responseDTO, existingPost.getRank());
        return (responseDTO);
    }

    /**************************************************************************
     * @description  : Downvote a post by decrementing its vote count.
     * @param postId : The ID of the post to downvote.
     * @return       : The downvoted post as a response DTO.
     * @cache        : The result returned by this method is cached.
     * @throws ValidationException : If the maximum votes have been reached.
    **************************************************************************/
    @Override
    public NewsPostResponseDTO downvotePost(Long postId) throws ValidationException
    {
        NewsPostSchema existingPost = getPostEntityById(postId);
        if(0 == existingPost.getVotes())
        {
            throw (new ValidationException("Cannot downvote below zero."));
        }

        existingPost.downVote();

        NewsPostResponseDTO responseDTO = modelMapper.map
        (newsPostRepository.save(existingPost), NewsPostResponseDTO.class);

        cacheService.put(responseDTO, existingPost.getRank());
        return (responseDTO);
    }

    /**************************************************************************
     * @description  : Private helper method to get a post by its ID to promote 
     *               : code reusability.
     * @param postId : The ID of the post to retrieve.
     * @return       : The retrieved post entity.
     * @throws NewsPostNotFoundException : If the specified post does not exist.
    **************************************************************************/
    private NewsPostSchema getPostEntityById(Long postId) throws NewsPostNotFoundException
    {
        return (newsPostRepository.findById(postId).orElseThrow(() -> 
        new NewsPostNotFoundException("postId", postId, "post")));
    }
}

 