package com.akamai.MiniHackerNews.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record Content(
    Integer id,
    @NotBlank
    String title,
    @NotBlank
    @Max(1024)
    String desc,
    @Max(8096)
    @NotEmpty
    Status status,
    Type contentType,
    LocalDateTime dateCreated,
    LocalDateTime dateUpdated, 
    String url
)
{
    
}
