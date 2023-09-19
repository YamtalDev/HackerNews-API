package com.akamai.MiniHackerNews.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import com.akamai.MiniHackerNews.model.Content;
import com.akamai.MiniHackerNews.model.Status;
import com.akamai.MiniHackerNews.model.Type;

import jakarta.annotation.PostConstruct;

@Repository
public class ContentCollectionRepository 
{
    private final List<Content> contentList = new ArrayList<>();

    public ContentCollectionRepository()
    {
        // Empty
    }

    public List<Content> findAll()
    {
        return (contentList);
    }

    public Optional<Content> findById(Integer id)
    {
        return contentList.stream().filter(c -> c.id().equals(id)).findFirst();
    }

    @PostConstruct
    private void init()
    {
        Content content = new Content(
            1, 
            "First blog post", 
            "My first blog post", 
            Status.IDEA, Type.ARTICLE, 
            LocalDateTime.now(), 
            null, 
            "");
        contentList.add(content);
    }

    public void save(Content content)
    {
        contentList.removeIf(c -> c.id().equals(content.id()));
        contentList.add(content);
    }

    public boolean existsById(Integer id)
    {
        return (1 == contentList.stream().filter(c -> c.id().equals(id)).count());
    }

    public void delete(Integer id)
    {
        contentList.removeIf(c -> c.id().equals(id));
    }
}
