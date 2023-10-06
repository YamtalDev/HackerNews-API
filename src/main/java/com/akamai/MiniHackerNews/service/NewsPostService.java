package com.akamai.MiniHackerNews.service;

import java.util.List;

import com.akamai.MiniHackerNews.dto.*;

/******************************************************************************
 * @description : Service interface for managing posts.
******************************************************************************/

public interface NewsPostService
{
    public void deletePost(Long postId);
    public NewsPostResponseDTO saveNewPost(NewsPostRequestDTO newsPost);

    public NewsPostResponseDTO getPostById(Long postId);

    public NewsPostResponseDTO upvotePost(Long postId);
    public NewsPostResponseDTO downvotePost(Long postId);

    public List<NewsPostResponseDTO> getAllPosts();
    public List<NewsPostResponseDTO> getPostsByRankDesc();

    public NewsPostResponseDTO updatePost(NewsPostRequestDTO newPost, Long postId);
    public NewsPostResponseDTO changePost(NewsUpdateRequestDTO newsPostDTO, Long postId);
}
