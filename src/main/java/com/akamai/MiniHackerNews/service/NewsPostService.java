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
