package com.akamai.MiniHackerNews.config;

import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;

public class CacheEntity
{
    private NewsPostResponseDTO entity;
    private Double rank;

    public CacheEntity(NewsPostResponseDTO entity, Double rank)
    {
        this.entity = entity;
        this.rank = rank;
    }

    public NewsPostResponseDTO getEntity()
    {
        return (entity);
    }

    public Double getRank()
    {
        return (rank);
    }
}
