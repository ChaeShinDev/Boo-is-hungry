package com.chaeshin.boo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BooApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooApplication.class, args);
    }

}
