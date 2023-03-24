package com.m1a2st.simplebackendpractice.config.security;

import com.m1a2st.simplebackendpractice.user.repository.UserProfileRepository;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author m1a2st
 * @Date 2023/3/21
 * @Version v1.0
 */
@Slf4j
@Component
public class SpringUserDetailsService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;

    public SpringUserDetailsService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile userProfile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("This user does not exist"));
        return new User(userProfile.getUsername(), userProfile.getPassword(),
                List.of(new SimpleGrantedAuthority(userProfile.getRole().name())));
    }
}
