package com.akamai.HackerNews.UnitTests;

import com.akamai.HackerNews.controller.NewsPostController;
import com.akamai.HackerNews.dto.NewsPostRequestDTO;
import com.akamai.HackerNews.dto.NewsPostResponseDTO;
import com.akamai.HackerNews.exception.ValidationException;
import com.akamai.HackerNews.service.NewsPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

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

    @Test
    public void testSaveNewsPost()
    {
        NewsPostRequestDTO requestDTO = new NewsPostRequestDTO();
        requestDTO.setPostedBy("Test user");
        requestDTO.setPost("Test post");
        requestDTO.setLink("http://test.com");

        NewsPostResponseDTO expectedResponseDTO = new NewsPostResponseDTO();
        expectedResponseDTO.setPostedBy("Test user");
        expectedResponseDTO.setPost("Test post");
        expectedResponseDTO.setLink("http://test.com");
        expectedResponseDTO.setTimeElapsed("Just now");
        expectedResponseDTO.setVotes(0);
        expectedResponseDTO.setPostId((long)1);

        given(newsPostService.saveNewPost(requestDTO)).willReturn(expectedResponseDTO);

        ResponseEntity<NewsPostResponseDTO> responseEntity = newsPostController.saveNewsPost(requestDTO);

        HttpStatus expectedStatus = HttpStatus.CREATED;

        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(expectedResponseDTO, responseEntity.getBody());
    }


    @Test
    public void testSaveInvalidNewsPost() {
        NewsPostRequestDTO requestDTO = new NewsPostRequestDTO();
        requestDTO.setPostedBy("");
        requestDTO.setPost("Test post");
        requestDTO.setLink("http://test.com");

        doThrow(ValidationException.class).when(newsPostService).saveNewPost(requestDTO);

        ResponseEntity<NewsPostResponseDTO> responseEntity = newsPostController.saveNewsPost(requestDTO);

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertEquals(expectedStatus, responseEntity.getStatusCode());
    }
}
