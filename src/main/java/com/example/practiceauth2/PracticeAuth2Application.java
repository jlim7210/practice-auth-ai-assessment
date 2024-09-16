package com.example.practiceauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
public class PracticeAuth2Application {

    public static void main(String[] args) {
        SpringApplication.run(PracticeAuth2Application.class, args);
    }

}
