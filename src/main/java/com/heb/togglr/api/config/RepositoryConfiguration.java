package com.heb.togglr.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.heb.togglr.api.handlers.UpdateEventHandlers;

@Configuration
public class RepositoryConfiguration {


    public RepositoryConfiguration(){
        super();
    }


    @Bean
    UpdateEventHandlers updateEventHandlers(){
        return new UpdateEventHandlers();
    }
}
