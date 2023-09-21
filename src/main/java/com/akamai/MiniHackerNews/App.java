package com.akamai.MiniHackerNews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/******************************************************************************
 * @author Tal Aharon
 * @version 1.0.0
 * @license MIT
 * @since 19/9/2023
******************************************************************************/

@EnableCaching
@SpringBootApplication
public class App 
{
	public static void main(String[] args)
	{
		SpringApplication.run(App.class, args);
	}
}
