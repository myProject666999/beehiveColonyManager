package com.beehive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.beehive.entity")
@EnableJpaRepositories(basePackages = "com.beehive.repository")
public class BeehiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeehiveApplication.class, args);
    }
}
