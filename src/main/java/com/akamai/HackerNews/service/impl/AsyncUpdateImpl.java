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

package com.akamai.HackerNews.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.akamai.HackerNews.repository.NewsPostRepository;
import com.akamai.HackerNews.schema.NewsPostSchema;
import com.akamai.HackerNews.service.AsyncUpdate;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/******************************************************************************
 * @description : Implementation of the AsyncUpdate service responsible to 
 * performing asynchronous updates for the app every configurable fixed time.
*******************************************************************************/

@Service
public class AsyncUpdateImpl implements AsyncUpdate
{
    private final NewsPostsCacheServiceImpl cache;
    private final NewsPostRepository newsPostRepository;

    public AsyncUpdateImpl(NewsPostsCacheServiceImpl cache, NewsPostRepository newsPostRepository)
    {
        this.cache = cache;
        this.newsPostRepository = newsPostRepository;
    }

    /**************************************************************************
     * @description: Asynchronously updates posts at regular intervals. 
     * {@interval: every hour} This method is scheduled to run at a fixed rate 
     * and have a initial delay.
    **************************************************************************/
    @Async
    @Override
    @Scheduled(initialDelay = 0, fixedRateString = "${app.async.update.interval}")
    public void updateAsync()
    {
        List<NewsPostSchema> allPosts = newsPostRepository.findAll();
        for(NewsPostSchema post : allPosts)
        {
            post.update();
        }

        newsPostRepository.saveAll(allPosts);
    }


    /**************************************************************************
     * @description: Asynchronously clears the cache at regular intervals. 
     * This method is scheduled to run at a configurable fixed rate based on the 
     * value set in the property file.
    **************************************************************************/
    @Async
    @Override
    @Scheduled(fixedRateString = "${app.async.update.interval}")
    public void clearCache()
    {
        cache.evictAll();
    }
}
