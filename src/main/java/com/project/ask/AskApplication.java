package com.project.ask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AskApplication {

    public static void main(String[] args) {
        SpringApplication.run(AskApplication.class, args);
    }

}
