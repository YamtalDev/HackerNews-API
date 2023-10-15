package com.akamai.HackerNews.UnitTests;

import com.akamai.HackerNews.dto.NewsPostRequestDTO;
import com.akamai.HackerNews.service.NewsPostService;
import com.akamai.HackerNews.dto.NewsPostResponseDTO;
import com.akamai.HackerNews.dto.NewsUpdateRequestDTO;
import com.akamai.HackerNews.controller.NewsPostController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;

public class ControllerTests
{
    @InjectMocks
    private NewsPostController newsPostController;

    @Mock
    private NewsPostService newsPostService;

    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    private NewsPostRequestDTO getRequest()
    {
        NewsPostRequestDTO requestDTO = new NewsPostRequestDTO();
        requestDTO.setPostedBy("Test user");
        requestDTO.setPost("Test post");
        requestDTO.setLink("http://test.com");
        return (requestDTO);
    }

    @Test
    public void saveNewsPostTest()
    {
        NewsPostRequestDTO requestDTO = getRequest();
        NewsPostResponseDTO expectedResponseDTO = new NewsPostResponseDTO();
        expectedResponseDTO.setPostedBy("Test user");
        expectedResponseDTO.setPost("Test post");
        expectedResponseDTO.setLink("http://test.com");
        expectedResponseDTO.setTimeElapsed("Just now");
        expectedResponseDTO.setVotes(0);
        expectedResponseDTO.setPostId(1L);

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
    public void changePostTest() {
        Long postId = 1L;
        NewsUpdateRequestDTO changedPost = new NewsUpdateRequestDTO();
        changedPost.setPost("Changed post");

        NewsPostResponseDTO expectedResponseDTO = new NewsPostResponseDTO();
        expectedResponseDTO.setPostId(postId);
        given(newsPostService.changePost(changedPost, postId)).willReturn(expectedResponseDTO);

        ResponseEntity<NewsPostResponseDTO> responseEntity = newsPostController.changePost(changedPost, postId);

        HttpStatus expectedStatus = HttpStatus.OK;
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(expectedResponseDTO, responseEntity.getBody());
    }

    @Test
    public void getAllPostsTest() {
        // Create a list of NewsPostResponseDTO objects for testing
        List<NewsPostResponseDTO> testNewsPosts = new ArrayList<>();
        // Populate the testNewsPosts list with data
        // Mock the service to return the list of news posts
        given(newsPostService.getAllPosts()).willReturn(testNewsPosts);

        ResponseEntity<List<NewsPostResponseDTO>> responseEntity = newsPostController.getAllPosts();

        HttpStatus expectedStatus = HttpStatus.OK;
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(testNewsPosts, responseEntity.getBody());
    }

    @Test
    public void getTopPostsByRankTest() {
        // Create a list of NewsPostResponseDTO objects for testing
        List<NewsPostResponseDTO> testTopPosts = new ArrayList<>();
        // Populate the testTopPosts list with data
        // Mock the service to return the list of top news posts
        given(newsPostService.getPostsByRankDesc()).willReturn(testTopPosts);

        ResponseEntity<List<NewsPostResponseDTO>> responseEntity = newsPostController.getTopPostsByRank();

        HttpStatus expectedStatus = HttpStatus.OK;
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(testTopPosts, responseEntity.getBody());
    }

    @Test
    public void upvotePostTest() {
        Long postId = 1L;
        NewsPostResponseDTO expectedResponseDTO = new NewsPostResponseDTO();
        // Set expected values in the response DTO
        expectedResponseDTO.setPostId(postId);
        // Mock the service to return the expected DTO
        given(newsPostService.upvotePost(postId)).willReturn(expectedResponseDTO);

        ResponseEntity<NewsPostResponseDTO> responseEntity = newsPostController.upvotePost(postId);

        HttpStatus expectedStatus = HttpStatus.OK;
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(expectedResponseDTO, responseEntity.getBody());
    }

    @Test
    public void downvotePostTest() {
        Long postId = 1L;
        NewsPostResponseDTO expectedResponseDTO = new NewsPostResponseDTO();
        // Set expected values in the response DTO
        expectedResponseDTO.setPostId(postId);
        // Mock the service to return the expected DTO
        given(newsPostService.downvotePost(postId)).willReturn(expectedResponseDTO);

        ResponseEntity<NewsPostResponseDTO> responseEntity = newsPostController.downvotePost(postId);

        HttpStatus expectedStatus = HttpStatus.OK;
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(expectedResponseDTO, responseEntity.getBody());
    }

}
