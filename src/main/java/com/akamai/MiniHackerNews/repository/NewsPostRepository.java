package com.akamai.MiniHackerNews.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akamai.MiniHackerNews.schema.NewsPost;

public interface NewsPostRepository extends JpaRepository<NewsPost, Long> {}