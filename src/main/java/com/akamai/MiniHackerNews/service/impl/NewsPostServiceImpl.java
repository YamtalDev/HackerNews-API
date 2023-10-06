package com.akamai.MiniHackerNews.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.akamai.MiniHackerNews.dto.*;
import com.akamai.MiniHackerNews.schema.*;
import com.akamai.MiniHackerNews.repository.*;
import com.akamai.MiniHackerNews.service.NewsPostService;
import com.akamai.MiniHackerNews.exception.ValidationException;
import com.akamai.MiniHackerNews.exception.NewsPostNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

/******************************************************************************
 * @description : Implementation of the NewsPostService responsible for handling 
 *                news posts data base entity operations. Provide the functionality 
 *                for the controllers layer. The methods in this service layer
 *                are using the repository interface methods. (JPA Repository).
 * 
 * @exceptions  : All exceptions thrown from the service functions are handled 
 *                by the global exception manager.
******************************************************************************/
@Service
public class NewsPostServiceImpl implements NewsPostService
{
    private ModelMapper modelMapper;
    HackerNewsCacheServiceImpl cacheService;
    private NewsPostRepository newsPostRepository;

    /**************************************************************************
     * @param modelMapper        : Injected ModelMapper instance.
     * @param newsPostRepository : Injected NewsPostRepository instance.
    **************************************************************************/
    public NewsPostServiceImpl
    (ModelMapper modelMapper, HackerNewsCacheServiceImpl cacheService, NewsPostRepository newsPostRepository)
    {
        this.modelMapper = modelMapper;
        this.cacheService = cacheService;
        this.newsPostRepository = newsPostRepository;
    }

    /**************************************************************************
     * @description       : Create and saves a new post based on the provided DTO object.
     * @param newsPostDTO : The DTO containing the new post data.
     * @return            : The saved news post as a mapped response DTO object.
     * @throws ValidationException : If the input data is invalid.
    **************************************************************************/
    @Override
    public NewsPostResponseDTO saveNewPost
    (NewsPostRequestDTO newsPostDTO) throws ValidationException
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

    /**************************************************************************
     * @description  : Delete a post based on its id.
     * @param postId :The ID of the post to be deleted.
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
    }

    /**************************************************************************
     * @description : Retrieve a post by its ID.
     * @return      : The retrieved post as a response DTO.
     * @throws NewsPostNotFoundException : If the specified post does not exist.
    **************************************************************************/
    @Override
    public NewsPostResponseDTO getPostById
    (Long postId) throws NewsPostNotFoundException
    {
        return (modelMapper.map(getPostEntityById(postId),NewsPostResponseDTO.class));
    }

    /**************************************************************************
     * @description    : Retrieves a paginated list of all news posts.
     * @return         : A list containing news posts as response DTOs.
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
     * Retrieves the top news posts ordered by rank in descending order.
     * @return A list of the top news posts in descending rank order.
    **************************************************************************/
    @Override
    public List<NewsPostResponseDTO> getPostsByRankDesc()
    {
        List<NewsPostSchema> topPosts = newsPostRepository.findByOrderByRankDesc();

        return(topPosts.stream()
        .map(newsPost -> modelMapper.map(newsPost, NewsPostResponseDTO.class))
        .collect(Collectors.toList()));
    }

    /**************************************************************************
     * @description       : Updates an existing news post with new data.
     * @param newsPostDTO : The DTO containing updated post data.
     * @throws ValidationException : If the input data is invalid.
    **************************************************************************/
    @Override
    public NewsPostResponseDTO updatePost
    (NewsPostRequestDTO newsPostDTO, Long postId) throws ValidationException
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

    /**************************************************************************
     * @description : Changes the data of an existing news post.
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
            return (modelMapper.map(newsPostRepository.save(existingPost), NewsPostResponseDTO.class));
        }
        catch(DataIntegrityViolationException |  NewsPostNotFoundException exception)
        {
            throw (new ValidationException("Invalid input data."));
        }
    }

    /**************************************************************************
     * @description : Up vote a post by incrementing its vote count.
     * @return      : The up voted post as a response DTO.
     * @throws ValidationException : If the maximum votes have been reached.
     *
     * @implNote    : It can be a better approach to implement a specific query to upvote
     *                or down vote directly without fetching the entity from the data base.
     * 
     * @implNote    : When a post votes is decremented below 0 ot incremented above 
     *                the max value, a low level sql exception arises due to the 
     *                restrictions in the schema. Currently there is an if statement 
     *                that checks the boundaries and throw a more higher level exception 
     *                to the exception manager to handle. other wise a jpa transaction 
     *                error occur. I need to think how to catch it without using if().
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
        return (modelMapper.map(newsPostRepository.save(existingPost), NewsPostResponseDTO.class));
    }

    /**************************************************************************
     * @description : Down vote a post by decrementing its vote count.
     * @return      : The up voted post as a response DTO.
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
        return (modelMapper.map(newsPostRepository.save(existingPost), NewsPostResponseDTO.class));
    }

    /**************************************************************************
     * @description: : Private helper method to get a post by its id to promote code reusability.
     * @throws NewsPostNotFoundException : If the maximum votes have been reached.
    **************************************************************************/
    private NewsPostSchema getPostEntityById(Long postId) throws NewsPostNotFoundException
    {
        return (newsPostRepository.findById(postId).orElseThrow(() -> 
        new NewsPostNotFoundException("postId", postId, "post")));
    }
}

 