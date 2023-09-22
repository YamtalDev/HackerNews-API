package com.akamai.MiniHackerNews.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.akamai.MiniHackerNews.schema.NewsPostSchema;
import org.springframework.data.jpa.repository.JpaRepository;

/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
 * @description :

******************************************************************************/
public interface NewsPostRepository extends JpaRepository<NewsPostSchema, Long> 
{
    Page<NewsPostSchema> findAll(Pageable pageable);
    Page<NewsPostSchema> findByOrderByRankDesc(Pageable pageable);
}