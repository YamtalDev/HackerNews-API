package com.akamai.MiniHackerNews;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class App 
{
	public static void main(String[] args)
	{
		SpringApplication.run(App.class, args);
	}
}
