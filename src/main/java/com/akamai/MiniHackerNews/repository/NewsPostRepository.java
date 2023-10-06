package com.akamai.MiniHackerNews.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.akamai.MiniHackerNews.schema.NewsPostSchema;

/******************************************************************************
 * @description : Repository interface for managing NewsPostSchema entities.
******************************************************************************/

public interface NewsPostRepository extends JpaRepository<NewsPostSchema, Long>
{
    /**************************************************************************
    * @description  : Retrieve a list of news posts ordered by rank in descending order. 
    *               : With a limit of 30 entities.
    * @return       : List of NewsPostSchema.
    **************************************************************************/
    @Query("SELECT p FROM NewsPostSchema p ORDER BY p.rank DESC LIMIT 30")
    List<NewsPostSchema> findByOrderByRankDesc();
}