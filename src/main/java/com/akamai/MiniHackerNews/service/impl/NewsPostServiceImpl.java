package com.akamai.MiniHackerNews.service.impl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.akamai.MiniHackerNews.schema.NewsPostEntity;
import com.akamai.MiniHackerNews.schema.dto.NewsPostRequest;
import com.akamai.MiniHackerNews.schema.dto.NewsPostResponse;
import com.akamai.MiniHackerNews.service.NewsPostService;
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
    private ModelMapper modelMapper;
    private NewsPostRepository newsPostRepository;

    public NewsPostServiceImpl(NewsPostRepository newsPostRepository)
    {
        this.newsPostRepository = newsPostRepository;
    }
    
    @Override
    public NewsPostResponse saveNewsPost(NewsPostRequest newsPostDTO) throws DataIntegrityViolationException
    {
        NewsPostEntity newsPost = new NewsPostEntity();
        newsPost.setLink(newsPostDTO.getLink());
        newsPost.setPost(newsPostDTO.getPost());
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
    public void deletePost(Long post_id)
    {
        getPostById(post_id);
        newsPostRepository.deleteById(post_id);
    }

    @Override
    public NewsPostResponse getPostById(Long post_id) throws ResourceNotFoundException
    {
        return (newsPostRepository.findById(post_id).orElseThrow(() -> 
        new ResourceNotFoundException("post_id", post_id, "post")));
    }

    @Override
    public Page<NewsPostResponse> getAllPosts(Pageable pageable)
    {
        return (newsPostRepository.findAll(pageable));
    }

    @Override
    public NewsPostResponse updatePost(NewsPostRequest newsPostDTO, Long post_id) throws DataIntegrityViolationException, ResourceNotFoundException
    {
        try
        {
            NewsPostEntity existingPost = getPostById(post_id);
            existingPost.setLink(newsPostDTO.getLink());
            existingPost.setPost(newsPostDTO.getPost());
            existingPost.setUserName(newsPostDTO.getUserName());
            return (newsPostRepository.save(existingPost));
        }
        catch (DataIntegrityViolationException |  ResourceNotFoundException exception)
        {
            throw (new ValidationException("Invalid input data."));
        }
    }


    @Override
    public Page<NewsPostResponse> getPostsByRankDesc(Pageable pageable)
    {
        return (newsPostRepository.findByOrderByRankDesc(pageable));
    }

    @Override
    public NewsPostResponse upvotePost(Long post_id) throws DataIntegrityViolationException, ResourceNotFoundException
    {
        try
        {
            NewsPostEntity existingPost = getPostById(post_id);
            existingPost.setVotes(existingPost.getVotes() + 1);
            return (newsPostRepository.save(existingPost));
        }
        catch(DataIntegrityViolationException | ResourceNotFoundException exception)
        {
            throw new ValidationException("Maximum votes reached.");
        }
    }

    @Override
    public NewsPostResponse downvotePost(Long post_id) throws DataIntegrityViolationException, ResourceNotFoundException
    {
        try
        {
            NewsPostEntity existingPost = getPostById(post_id);
            existingPost.setVotes(existingPost.getVotes() - 1);
            return (newsPostRepository.save(existingPost));
        }
        catch(DataIntegrityViolationException | ResourceNotFoundException exception)
        {
            throw new ValidationException("Cannot downvote below zero.");
        }
    }
}
