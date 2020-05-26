package com.heb.togglr.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@ComponentScan(basePackages = {"com.heb.togglr"})
@EnableAsync
public class TogglrApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TogglrApiApplication.class, args);
    }
}
