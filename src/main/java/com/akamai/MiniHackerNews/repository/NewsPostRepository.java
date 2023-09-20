package com.akamai.MiniHackerNews.repository;

import com.akamai.MiniHackerNews.model.NewsPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsPostRepository extends JpaRepository<NewsPost, Long>
{
    
}
