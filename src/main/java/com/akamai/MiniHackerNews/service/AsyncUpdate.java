package com.akamai.MiniHackerNews.service;

import org.springframework.scheduling.annotation.Async;

/******************************************************************************
 * @description : Service interface for performing asynchronous updates.
******************************************************************************/

 public interface AsyncUpdate
{
    @Async
    public void updateAsync();
}
