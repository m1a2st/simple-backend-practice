package com.m1a2st.simplebackendpractice.config.security;

import com.m1a2st.simplebackendpractice.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.m1a2st.simplebackendpractice.utils.ResponseUtils.responseJsonWriter;

/**
 * @Author m1a2st
 * @Date 2023/3/21
 * @Version v1.0
 */
@Configuration
@Slf4j
public class SecurityBean {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler customerASHandler() {
        return (request, response, authentication) -> {
            if (response.isCommitted()) {
                log.debug("Response is already commit");
            }
            Map<String, String> map = new HashMap<>(){{
                put("Time", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(LocalDateTime.now()));
                put("msg", "success_login");
                put("name",authentication.getName());
            }};
            String token = JwtTokenUtils.generateToken(map);
            responseJsonWriter(response, token);
        };
    }

    @Bean
    public AuthenticationFailureHandler customerAfHandler() {
        return (request, response, exception) -> responseJsonWriter(response, "login fail");
    }

    @Bean
    public LogoutHandler customerLogoutHandler() {
        return (request, response, authentication) -> {
            log.info("logout handler is running....");
        };
    }

    @Bean
    public LogoutSuccessHandler customerLogoutSuccessHandler() {
        return (request, response, authentication) -> responseJsonWriter(response, authentication.getName());
    }
}
