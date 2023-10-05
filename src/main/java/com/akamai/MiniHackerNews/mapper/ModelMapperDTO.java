package com.akamai.MiniHackerNews.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/******************************************************************************
 * @description : ModelMapper instance with strict matching strategy to map 
 *              : between the DTO objects(response & request) to schema and back.
 * 
 * @apiNote     : The matching strategy of the json fields is set to STRICT.
******************************************************************************/
@Configuration
public class ModelMapperDTO
{
    @Bean
    public ModelMapper modelMapper()
    {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return (modelMapper);
    }
}
