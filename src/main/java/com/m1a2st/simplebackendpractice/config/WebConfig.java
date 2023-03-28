package com.m1a2st.simplebackendpractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Author m1a2st
 * @Date 2023/3/28
 * @Version v1.0
 */
@Configuration
public class WebConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
