package com.heb.togglr.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;


@EnableResourceServer
@SpringBootApplication
@ComponentScan(basePackages = {"com.heb.togglr"})
public class TogglrApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TogglrApiApplication.class, args);
    }
}
