package com.akamai.MiniHackerNews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.akamai.MiniHackerNews.schema.NewsPost;

/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
 * @description :

******************************************************************************/
public interface NewsPostRepository extends JpaRepository<NewsPost, Long> 
{
    Page<NewsPost> findAll(Pageable pageable);
}