package com.akamai.MiniHackerNews;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EnableAsync
@EnableCaching
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "com.akamai.MiniHackerNews")
@EntityScan(basePackages = "com.akamai.MiniHackerNews.schema")
public class App 
{
	public static void main(String[] args)
	{
		SpringApplication.run(App.class, args);
	}
}
