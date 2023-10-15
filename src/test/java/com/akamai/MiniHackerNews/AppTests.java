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
package com.akamai.MiniHackerNews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.akamai.MiniHackerNews.dto.NewsPostRequestDTO;
import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;

@SpringBootTest
@AutoConfigureMockMvc
public class AppTests
{

    private URL base;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:8080/api/news");
        restTemplate = restTemplateBuilder.rootUri(base.toString()).build();
    }

    @Test
    void testSaveNewsPostEndpoint()
    {
        String apiUrl = "http://localhost:8080/api/news";

        NewsPostRequestDTO newsPost = new NewsPostRequestDTO();
        newsPost.setPostedBy("User");
        newsPost.setPost("Post Content");
        newsPost.setLink("https://example.com/test");

        ResponseEntity<NewsPostResponseDTO> responseEntity = restTemplate.postForEntity(apiUrl, newsPost, NewsPostResponseDTO.class);

        assertTrue(responseEntity.getStatusCode() == HttpStatus.CREATED);

        NewsPostResponseDTO response = responseEntity.getBody();
        assertNotNull(response, "Response should not be null");

        String errorMsg = " does not match";
        assertEquals("User", response.getPostedBy(), "User name" + errorMsg);
        assertEquals("Post Content", response.getPost(), "Posts content" + errorMsg);
        assertEquals("https://example.com/test", response.getLink(), "Link" + errorMsg);
        assertEquals("Just now", response.getTimeElapsed(), "Time elapsed" + errorMsg);
        assertEquals(0, response.getVotes(), "Votes " + errorMsg);
    }

    @Test
    public void testUpdateNewsPost() throws Exception
    {

    }

    @Test
    public void testChangeNewsPost() throws Exception
    {

    }

    @Test
    public void testGetNewsPostById() throws Exception
    {
    }

    @Test
    public void testGetAllNewsPosts() throws Exception
    {

    }

    @Test
    public void testGetTopPosts() throws Exception
    {

    }

    @Test
    public void testDeleteNewsPost() throws Exception
    {

    }

    @Test
    public void testUpvoteNewsPost() throws Exception
    {

    }

    @Test
    public void testDownvoteNewsPost() throws Exception
    {

    }

    @Test
    public void testAsyncUpdate() throws Exception
    {

    }

    @Test
    public void testBenchMarkOfTopPosts() throws Exception
    {

    }

    @Test
    public void testCacheTopPosts() throws Exception
    {

    }

    @Test
    public void testExceptions() throws Exception
    {

    }
}
