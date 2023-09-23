/******************************************************************************
MIT License

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
package com.akamai.MiniHackerNews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.akamai.MiniHackerNews.schema.NewsPostSchema; /* Internal API */

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