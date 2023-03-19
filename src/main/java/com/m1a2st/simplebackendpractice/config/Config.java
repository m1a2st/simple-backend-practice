package com.m1a2st.simplebackendpractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @Author m1a2st
 * @Date 2023/3/19
 * @Version v1.0
 */
@Configuration
@EnableJpaAuditing
public class Config {

    @Bean
    public SpringSecurityAuditorAware springSecurityAuditorAware(){
        return new SpringSecurityAuditorAware();
    }
}
