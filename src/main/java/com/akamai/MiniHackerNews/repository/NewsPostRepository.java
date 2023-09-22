package com.akamai.MiniHackerNews.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.akamai.MiniHackerNews.schema.NewsPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
 * @description :

******************************************************************************/
public interface NewsPostRepository extends JpaRepository<NewsPostEntity, Long> 
{
    Page<NewsPostEntity> findAll(Pageable pageable);
    Page<NewsPostEntity> findByOrderByRankDesc(Pageable pageable);
}