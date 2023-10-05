package com.akamai.MiniHackerNews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.akamai.MiniHackerNews.schema.NewsPostSchema;

import org.springframework.data.jpa.repository.JpaRepository;

/******************************************************************************
 * @description : Repository interface for managing NewsPostSchema entities.
******************************************************************************/

public interface NewsPostRepository extends JpaRepository<NewsPostSchema, Long> 
{
    /**************************************************************************
    * @description    : Retrieve a pageable list of all news posts.
    * @param pageable : The pagination information.
    * @return         : A Page containing news posts.
    **************************************************************************/
    Page<NewsPostSchema> findAll(Pageable pageable);

    /**************************************************************************
    * @description : Retrieve a pageable list of news posts ordered by rank in descending order.
    * @Query       : Custom JPQL for this repository method.
    **************************************************************************/
    @Query("SELECT p FROM NewsPostSchema p ORDER BY p.rank DESC")
    Page<NewsPostSchema> findByOrderByRankDesc(Pageable pageable);
}