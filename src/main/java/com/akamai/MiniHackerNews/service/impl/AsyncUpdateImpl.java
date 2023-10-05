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
