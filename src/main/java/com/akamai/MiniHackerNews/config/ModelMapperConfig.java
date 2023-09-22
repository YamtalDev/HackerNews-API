package com.akamai.MiniHackerNews.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig
{

    @Bean
    public ModelMapper modelMapper()
    {
        ModelMapper modelMapper = new ModelMapper();
        
        // Configure the matching strategy, use STRICT to enforce exact property mapping
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        // Add custom mappings or converters if needed
        // modelMapper.addMappings(myCustomMappings());

        return modelMapper;
    }
}


        // if (hoursDifference < 1) {
        //     postTime = "Just now";
        // } else if (hoursDifference < 24) {
        //     postTime = hoursDifference + " hour" + (hoursDifference == 1 ? "" : "s") + " ago";
        // } else {
        //     long daysDifference = hoursDifference / 24;
        //     postTime = daysDifference + " day" + (daysDifference == 1 ? "" : "s") + " ago";
        // }
