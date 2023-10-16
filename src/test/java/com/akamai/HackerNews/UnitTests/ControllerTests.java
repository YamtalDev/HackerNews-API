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

package com.akamai.HackerNews.UnitTests;

import java.util.List;
import java.util.ArrayList;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.akamai.HackerNews.dto.NewsPostRequestDTO;
import com.akamai.HackerNews.service.NewsPostService;
import com.akamai.HackerNews.dto.NewsPostResponseDTO;
import com.akamai.HackerNews.dto.NewsUpdateRequestDTO;
import com.akamai.HackerNews.controller.NewsPostController;

/******************************************************************************
 * @description: Unit tests for the NewsPostController class. These tests validate
 * the controller's behavior for handling CRUD operations and managing news posts.
******************************************************************************/

public class ControllerTests
{
    @InjectMocks
    private NewsPostController newsPostController;

    @Mock
    private NewsPostService newsPostService;

    @Test
    public void saveNewsPostTest()
    {
        NewsPostRequestDTO requestDTO = getNewRequest();
        NewsPostResponseDTO expectedResponseDTO = getNewResponse();

        given(newsPostService.saveNewPost(requestDTO)).willReturn(expectedResponseDTO);

        ResponseEntity<NewsPostResponseDTO> responseEntity = newsPostController.saveNewsPost(requestDTO);

        HttpStatus expectedStatus = HttpStatus.CREATED;

        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(expectedResponseDTO, responseEntity.getBody());
    }

    @Test
    public void getPostByIdTest()
    {
        Long postId = 1L;
        NewsPostResponseDTO expectedResponseDTO = new NewsPostResponseDTO();
        expectedResponseDTO.setPostId(postId);

        given(newsPostService.getPostById(postId)).willReturn(expectedResponseDTO);
    
        ResponseEntity<NewsPostResponseDTO> responseEntity = newsPostController.getPostById(postId);
    
        HttpStatus expectedStatus = HttpStatus.FOUND;
    
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(expectedResponseDTO, responseEntity.getBody());
    }

    @Test
    public void updatePostTest()
    {
        NewsPostRequestDTO updatedPost = new NewsPostRequestDTO();
        updatedPost.setPostedBy("Updated user");
        updatedPost.setPost("Updated post");
        updatedPost.setLink("http://updated.com");
    
        NewsPostResponseDTO expectedResponseDTO = new NewsPostResponseDTO();
        expectedResponseDTO.setPostedBy("Updated user");
        expectedResponseDTO.setPost("Updated post");
        expectedResponseDTO.setLink("http://updated.com");
        expectedResponseDTO.setTimeElapsed("Just now");
        expectedResponseDTO.setVotes(0);
        expectedResponseDTO.setPostId(1L);


        given(newsPostService.updatePost(updatedPost, 1L)).willReturn(expectedResponseDTO);
        ResponseEntity<NewsPostResponseDTO> responseEntity = newsPostController.updatePost(updatedPost, 1L);
        HttpStatus expectedStatus = HttpStatus.OK;
    
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(expectedResponseDTO, responseEntity.getBody());
    }

    @Test
    public void deletePostTest()
    {
        Long postId = 1L;

        ResponseEntity<String> responseEntity = newsPostController.deletePost(postId);

        HttpStatus expectedStatus = HttpStatus.OK;
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals("Post deleted", responseEntity.getBody());
    }

    @Test
    public void changePostTest()
    {
        NewsUpdateRequestDTO changedPost = new NewsUpdateRequestDTO();
        changedPost.setPost("Changed post");
        changedPost.setLink("http://changed.com");

        NewsPostResponseDTO expectedResponseDTO = new NewsPostResponseDTO();
        expectedResponseDTO.setPostedBy("User");
        expectedResponseDTO.setPost("Changed post");
        expectedResponseDTO.setLink("http://changed.com");
        expectedResponseDTO.setTimeElapsed("Just now");
        expectedResponseDTO.setVotes(0);
        expectedResponseDTO.setPostId(1L);

        given(newsPostService.changePost(changedPost, 1L)).willReturn(expectedResponseDTO);

        ResponseEntity<NewsPostResponseDTO> responseEntity = newsPostController.changePost(changedPost, 1L);

        HttpStatus expectedStatus = HttpStatus.OK;
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(expectedResponseDTO, responseEntity.getBody());
    }

    @Test
    public void getAllPostsTest()
    {
        List<NewsPostResponseDTO> newsPosts = new ArrayList<NewsPostResponseDTO>();
        for(int i = 0; i < 100; ++i)
        {
            newsPosts.add(getNewResponse());
        }

        given(newsPostService.getAllPosts()).willReturn(newsPosts);

        ResponseEntity<List<NewsPostResponseDTO>> responseEntity = newsPostController.getAllPosts();

        HttpStatus expectedStatus = HttpStatus.OK;
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(newsPosts, responseEntity.getBody());
    }

    @Test
    public void upvotePostTest()
    {
        NewsPostResponseDTO expectedResponseDTO = getNewResponse();
        expectedResponseDTO.setVotes(1);

        given(newsPostService.upvotePost(1L)).willReturn(expectedResponseDTO);

        ResponseEntity<NewsPostResponseDTO> responseEntity = newsPostController.upvotePost(1L);

        HttpStatus expectedStatus = HttpStatus.OK;
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(expectedResponseDTO, responseEntity.getBody());
    }

    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    private NewsPostRequestDTO getNewRequest()
    {
        NewsPostRequestDTO requestDTO = new NewsPostRequestDTO();
        requestDTO.setPostedBy("Test user");
        requestDTO.setPost("Test post");
        requestDTO.setLink("http://test.com");
        return (requestDTO);
    }

    private NewsPostResponseDTO getNewResponse()
    {
        NewsPostResponseDTO expectedResponseDTO = new NewsPostResponseDTO();
        expectedResponseDTO.setPostedBy("Test user");
        expectedResponseDTO.setPost("Test post");
        expectedResponseDTO.setLink("http://test.com");
        expectedResponseDTO.setTimeElapsed("Just now");
        expectedResponseDTO.setVotes(0);
        expectedResponseDTO.setPostId(1L);
        return (expectedResponseDTO);
    } 
}
