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

package com.akamai.MiniHackerNews.cache;

import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;

/******************************************************************************
 * @description: Representation of a cache entity that holds a NewsPostResponseDTO 
 * and its associated rank. This class is used within the RankedCache to store and 
 * manage cache entries with their ranks.
******************************************************************************/
public class CacheEntity
{
    private NewsPostResponseDTO entity;
    private Double rank;

    public CacheEntity(NewsPostResponseDTO entity, Double rank)
    {
        this.rank = rank;
        this.entity = entity;
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
