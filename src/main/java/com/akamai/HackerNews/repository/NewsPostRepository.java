/******************************************************************************

Copyright (c) 2023 Tal Aharon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

******************************************************************************/

package com.akamai.HackerNews.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.akamai.HackerNews.schema.NewsPostSchema;

import org.springframework.data.jpa.repository.JpaRepository;

/******************************************************************************
 * @description : Repository interface for managing NewsPostSchema entities.
******************************************************************************/

public interface NewsPostRepository extends JpaRepository<NewsPostSchema, Long>
{
    /**************************************************************************
    * @description: Retrieve a list of news posts ordered by rank in descending 
    * order with configurable limit of entities fetched from the data base.
    *
    * @return: List of NewsPostSchema.
    **************************************************************************/
    @Query("SELECT p FROM NewsPostSchema p ORDER BY p.rank DESC LIMIT :limit")
    List<NewsPostSchema> findByOrderByRankDescWithLimit(@Param("limit") int limit);
}