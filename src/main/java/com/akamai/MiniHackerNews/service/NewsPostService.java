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
    public int upvotePost(Long post_id);
    public void deletePost(Long post_id);
    public int downvotePost(Long post_id);
    public NewsPostResponseDTO getPostById(Long post_id);
    public Page<NewsPostResponseDTO> getAllPosts(Pageable pageable);
    public NewsPostResponseDTO saveNewsPost(NewsPostRequestDTO newsPost);
    public Page<NewsPostResponseDTO> getPostsByRankDesc(Pageable pageable);
    public NewsPostResponseDTO updatePost(NewsPostRequestDTO newPost, Long post_id);
    public NewsPostResponseDTO changePost(NewsUpdateRequestDTO newsPostDTO, Long post_id);
}
