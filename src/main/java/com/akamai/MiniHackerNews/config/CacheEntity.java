package com.akamai.MiniHackerNews.config;

import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;

public class CacheEntity
{
    private NewsPostResponseDTO entity;
    private Double rank;

    public NewsPostResponseDTO getEntity()
    {
        return (entity);
    }

    public void setEntity(NewsPostResponseDTO entity)
    {
        this.entity = entity;
    }

    public Double getRank()
    {
        return (rank);
    }

    public void setRank(Double rank)
    {
        this.rank = rank;
    }
}
