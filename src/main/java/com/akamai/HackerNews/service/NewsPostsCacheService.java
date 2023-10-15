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

package com.akamai.HackerNews.service;

import java.util.List;

import com.akamai.HackerNews.dto.NewsPostResponseDTO;
import com.akamai.HackerNews.schema.NewsPostSchema;

/******************************************************************************
 * @description: Service interface responsible for caching and managing news posts.
 * The interface defines methods to store, retrieve, and clear cached news posts,
 * allowing access to frequently used data.
******************************************************************************/

public interface NewsPostsCacheService 
{
    public void evictAll();
    public void evict(Long postId);
    public NewsPostResponseDTO get(Long postId);
    public void put(NewsPostResponseDTO entity, Double rank);
    public void putTopPosts(List<NewsPostSchema> topPosts);
    public List<NewsPostResponseDTO> getTopPostsFromCache();
}
