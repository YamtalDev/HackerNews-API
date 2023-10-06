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

package com.akamai.MiniHackerNews.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.akamai.MiniHackerNews.repository.NewsPostRepository;
import com.akamai.MiniHackerNews.schema.NewsPostSchema;
import com.akamai.MiniHackerNews.service.AsyncUpdate;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/******************************************************************************
 * @description : Implementation of the AsyncUpdate service responsible to performing
 *              : asynchronous updates of the rank and elapsed time in the data base
 *              : every fixed time.
 *
 * @apiNote     : Maybe it is worth thinking of a way to configure the delay and 
 *              : timing from the yml file. For now the compiler shouts that he 
 *              : must accept const variables.
*******************************************************************************/

@Service
public class AsyncUpdateImpl implements AsyncUpdate
{
    private final NewsPostRepository newsPostRepository;
     
    public AsyncUpdateImpl(NewsPostRepository newsPostRepository)
    {
        this.newsPostRepository = newsPostRepository;
    }

    /**************************************************************************
     * Asynchronously updates posts at regular intervals. {@interval: every hour}
     * This method is scheduled to run at a fixed rate and have a initial delay.
    **************************************************************************/
    @Async
    @Override
    @Scheduled(initialDelay = 0, fixedRate = 3600000)
    public void updateAsync()
    {
        List<NewsPostSchema> allPosts = newsPostRepository.findAll();
        for(NewsPostSchema post : allPosts)
        {
            post.update();
        }

        newsPostRepository.saveAll(allPosts);
    }
}
