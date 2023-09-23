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

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;

import com.akamai.MiniHackerNews.schema.*;                            /* Internal Implementation */
import com.akamai.MiniHackerNews.repository.*;                        /* Internal Implementation */
import com.akamai.MiniHackerNews.schema.dto.*;                        /* Internal Implementation */
import com.akamai.MiniHackerNews.service.NewsPostService;             /* Internal Implementation */
import com.akamai.MiniHackerNews.exception.ValidationException;       /* Internal Implementation */
import com.akamai.MiniHackerNews.exception.NewsPostNotFoundException; /* Internal Implementation */

/******************************************************************************
 * @description : Implementation of the NewsPostService responsible for handling 
 *              : news posts data base entity operations. Provide the functionality 
 *              : for the controllers layer. The methods in this service layer
 *              : are using the repository interface methods. (JPA Repository).
 * 
 * @exceptions  : All exceptions thrown from the service functions are handled 
 *              : by the global exception manager.
******************************************************************************/
@Service
public class NewsPostServiceImpl implements NewsPostService
{
    private ModelMapper modelMapper;
    private NewsPostRepository newsPostRepository;

    /**************************************************************************
     * @param modelMapper        : Injected ModelMapper instance.
     * @param newsPostRepository : Injected NewsPostRepository instance.
    **************************************************************************/
    public NewsPostServiceImpl
    (ModelMapper modelMapper, NewsPostRepository newsPostRepository)
    {
        this.modelMapper = modelMapper;
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
    **************************************************************************/
    @Override
    public void deletePost(Long postId)
    {
        getPostById(postId);
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
     * @param pageable : The pagination information.
     * @return         : A page containing news posts as response DTOs.
     * @implNote       : This method could be implemented also with a simple list.
     *                 : Due to performance requirements pagination is chosen.
    **************************************************************************/
    @Override
    public Page<NewsPostResponseDTO> getAllPosts(Pageable pageable)
    {
        return (newsPostRepository.findAll(pageable)).map
        (entity -> modelMapper.map(entity, NewsPostResponseDTO.class));
    }

    /**************************************************************************
     * @description : Retrieves the top news posts ordered by rank in descending order.
     * @implNote    : This method could be implemented also with a simple list.
     *              : Due to performance requirements pagination is chosen.
    **************************************************************************/
    @Override
    public Page<NewsPostResponseDTO> getPostsByRankDesc(Pageable pageable)
    {
        return (newsPostRepository.findByOrderByRankDesc(pageable).map
        (entity -> modelMapper.map(entity, NewsPostResponseDTO.class)));
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

 