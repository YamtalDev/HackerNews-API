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

package com.akamai.HackerNews.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akamai.HackerNews.dto.*;
import com.akamai.HackerNews.service.NewsPostService;

/**************************************************************************
 * @description : Controller layer responsible for handling CRUD operations and 
 * managing news posts. This controller provides endpoints to create, retrieve, 
 * update, and delete news posts, as well as perform upvotes and downvotes on posts.
 *
 * @NewsPostRequestDTO   : DTO representation of the client post, put requests structure.
 * @NewsPostResponseDTO  : DTO to represent the structure of the response to the client side.
 * @NewsUpdateRequestDTO : DTO representation of the client patch request structure.
 * @caching              : Caching logic is implemented in the service layer.
**************************************************************************/

@Validated
@RestController
@RequestMapping("/api/news")
public class NewsPostController
{
    private NewsPostService newsService;

    public NewsPostController(NewsPostService newsService)
    {
        this.newsService = newsService;
    }

    /**************************************************************************
     * @description : Save a new post.
    **************************************************************************/
    @PostMapping("")
    public ResponseEntity<NewsPostResponseDTO> saveNewsPost
    (@Validated @RequestBody NewsPostRequestDTO newsPost)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.saveNewPost(newsPost), HttpStatus.CREATED));
    }

    /**************************************************************************
     * @description : Retrieve a news post by its ID.
    **************************************************************************/
    @GetMapping("/{postId}")
    public ResponseEntity<NewsPostResponseDTO> getPostById
    (@Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.getPostById(postId), HttpStatus.FOUND));
    }

    /**************************************************************************
     * @description : Update a post by its ID.
    **************************************************************************/
    @PutMapping("/{postId}")
    public ResponseEntity<NewsPostResponseDTO> updatePost
    (@Validated @RequestBody NewsPostRequestDTO updatedPost, @Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.updatePost(updatedPost, postId), HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Delete a post by its ID.
    **************************************************************************/
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost
    (@Validated @PathVariable("postId") Long postId)
    {
        newsService.deletePost(postId);
        return (new ResponseEntity<String>("Post deleted", HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Change a post by its ID.
    **************************************************************************/
    @PatchMapping("/{postId}")
    public ResponseEntity<NewsPostResponseDTO> changePost
    (@Validated @RequestBody NewsUpdateRequestDTO changedPost, @Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.changePost(changedPost, postId), HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Retrieve all news posts..
     * @return      : ResponseEntity List of news posts.
    **************************************************************************/
    @GetMapping("")
    public ResponseEntity<List<NewsPostResponseDTO>> getAllPosts()
    {
        List<NewsPostResponseDTO> newsPosts = newsService.getAllPosts();
        return (new ResponseEntity<List<NewsPostResponseDTO>>(newsPosts, HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Retrieve the top news posts by rank.
     * @return      : ResponseEntity List of the top news posts ordered by descending order.
     **************************************************************************/
    @GetMapping("/top-posts")
    public ResponseEntity<List<NewsPostResponseDTO>> getTopPostsByRank()
    {
        List<NewsPostResponseDTO> topPosts = newsService.getPostsByRankDesc();
        return (new ResponseEntity<List<NewsPostResponseDTO>>(topPosts, HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Upvote a post by its ID.
    **************************************************************************/
    @PatchMapping("/{postId}/upvote")
    public ResponseEntity<NewsPostResponseDTO> upvotePost
    (@Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.upvotePost(postId), HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Downvote a post by its ID.
    **************************************************************************/
    @PatchMapping("/{postId}/downvote")
    public ResponseEntity<NewsPostResponseDTO> downvotePost
    (@Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.downvotePost(postId), HttpStatus.OK));
    }
}