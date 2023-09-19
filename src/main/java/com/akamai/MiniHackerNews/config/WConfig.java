package com.akamai.MiniHackerNews.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.client.RestTemplateBuilder;

@Configuration
public class WConfig
{
    @Bean
    public RestTemplate restTemplate()
    {
        return (new RestTemplateBuilder().build());
    }
}
