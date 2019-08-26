package com.heb.togglr.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.heb.togglr.api.handlers.UpdateEventHandlers;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class RepositoryConfiguration {


    public RepositoryConfiguration(){
        super();
    }

    @Bean
    ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

    @Bean
    UpdateEventHandlers updateEventHandlers(){
        return new UpdateEventHandlers();
    }
}
