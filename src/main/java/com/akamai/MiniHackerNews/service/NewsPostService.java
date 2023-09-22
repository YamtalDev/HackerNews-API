package com.akamai.MiniHackerNews.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.akamai.MiniHackerNews.schema.dto.NewsPostRequestDTO;
import com.akamai.MiniHackerNews.schema.dto.NewsPostResponseDTO;
import com.akamai.MiniHackerNews.schema.dto.NewsUpdateRequestDTO;

/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
 * @description:  
******************************************************************************/

public interface NewsPostService
{
    public int upvotePost(Long postId);
    public void deletePost(Long postId);
    public int downvotePost(Long postId);
    public NewsPostResponseDTO getPostById(Long postId);
    public Page<NewsPostResponseDTO> getAllPosts(Pageable pageable);
    public NewsPostResponseDTO saveNewPost(NewsPostRequestDTO newsPost);
    public Page<NewsPostResponseDTO> getPostsByRankDesc(Pageable pageable);
    public NewsPostResponseDTO updatePost(NewsPostRequestDTO newPost, Long postId);
    public NewsPostResponseDTO changePost(NewsUpdateRequestDTO newsPostDTO, Long postId);
}
