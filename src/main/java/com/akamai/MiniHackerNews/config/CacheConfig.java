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

package com.akamai.MiniHackerNews.config;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import org.springframework.context.annotation.Bean;
import com.akamai.MiniHackerNews.dto.NewsPostResponseDTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.CachingConfigurer;

@Configuration
public class CacheConfig implements CachingConfigurer
{
    @Bean
    public ConcurrentHashMap<Long, NewsPostResponseDTO> cache()
    {
        return (new ConcurrentHashMap<Long, NewsPostResponseDTO>());
    }

    @Bean
    public ConcurrentSkipListMap<Double, Long> rankToPostIdMap()
    {
        return (new ConcurrentSkipListMap<Double, Long>((rank1, rank2) -> Double.compare(rank2, rank1)));
    }
}