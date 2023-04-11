package com.hanghae.navis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@PropertySource(value = "application.properties")
public class NavisApplication {

    public static void main(String[] args) {
        SpringApplication.run(NavisApplication.class, args);
    }

}
