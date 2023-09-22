package com.akamai.MiniHackerNews.service.impl;

import com.akamai.MiniHackerNews.service.AsyncUpdate;

import java.util.List;

import org.springframework.core.task.AsyncTaskExecutor;
import com.akamai.MiniHackerNews.repository.NewsPostRepository;
import com.akamai.MiniHackerNews.schema.NewsPostSchema;

public class AsyncUpdateImpl implements AsyncUpdate
{
    private final NewsPostRepository newsPostRepository;
    private final AsyncTaskExecutor asyncTaskExecutor;

    public AsyncUpdateImpl(NewsPostRepository newsPostRepository, AsyncTaskExecutor asyncTaskExecutor)
    {
        this.newsPostRepository = newsPostRepository;
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    @Override
    public void updateRanksAsync()
    {
        asyncTaskExecutor.submit(() ->
        {
            List<NewsPostSchema> allPosts = newsPostRepository.findAll();

            for(NewsPostSchema post : allPosts)
            {
                post.updateRank();
            }
        });
    }
}
