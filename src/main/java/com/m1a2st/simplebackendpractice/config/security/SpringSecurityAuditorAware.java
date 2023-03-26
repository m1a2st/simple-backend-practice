package com.m1a2st.simplebackendpractice.config.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


/**
 * @Author m1a2st
 * @Date 2023/3/19
 * @Version v1.0
 */
public class SpringSecurityAuditorAware implements AuditorAware<String> {

//    @Override
//    public Optional<String> getCurrentAuditor() {
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authentication = context.getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return Optional.of("");
//        }
//        return Optional.of(authentication.getName());
//    }

    @Override
    public Optional<String> getCurrentAuditor(){
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(s -> (AbstractAuthenticationToken)s)
                .map(AbstractAuthenticationToken::getName)
                .or(()->Optional.of(""));
    }
}
