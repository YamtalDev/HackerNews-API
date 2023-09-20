package com.akamai.MiniHackerNews.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.akamai.MiniHackerNews.schema.NewsPost;
import com.akamai.MiniHackerNews.service.NewsPostService;
import com.akamai.MiniHackerNews.dto.NewsPostUserRequestDTO;
import com.akamai.MiniHackerNews.exception.ResourceNotFoundException;
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
        newsPost.setTitle(newsPostDTO.getTitle());
        newsPost.setUserName(newsPostDTO.getUserName());
        newsPost.setLink(newsPostDTO.getLink());
        return newsPostRepository.save(newsPost);
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
    public NewsPost updatePost(NewsPostUserRequestDTO newPost, Long post_id)
    {
        NewsPost existingPost = getPostById(post_id);

        existingPost.setLink(newPost.getLink());
        existingPost.setTitle(newPost.getTitle());
        existingPost.setUserName(newPost.getUserName());
        return (newsPostRepository.save(existingPost));
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
        return (newsPostRepository.save(existingPost));
    }

    public NewsPost downvotePost(Long post_id)
    {
        NewsPost existingPost = getPostById(post_id);
        existingPost.setVotes(existingPost.getVotes() - 1);
        return (newsPostRepository.save(existingPost));
    }
}
