package com.m1a2st.simplebackendpractice.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author m1a2st
 * @Date 2023/3/20
 * @Version v1.0
 */

@EnableJpaAuditing
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SpringUserDetailsService springUserDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(SpringUserDetailsService springUserDetailsService, JwtTokenFilter jwtTokenFilter) {
        this.springUserDetailsService = springUserDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SpringSecurityAuditorAware springSecurityAuditorAware(){
        return new SpringSecurityAuditorAware();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(springUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .antMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1.0/user:login", "/api/v1.0/user:signup").permitAll()
                .anyRequest().authenticated();
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
