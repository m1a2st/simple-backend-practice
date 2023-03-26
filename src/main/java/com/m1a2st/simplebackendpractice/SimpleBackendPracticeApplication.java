package com.m1a2st.simplebackendpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SimpleBackendPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleBackendPracticeApplication.class, args);
    }

}
