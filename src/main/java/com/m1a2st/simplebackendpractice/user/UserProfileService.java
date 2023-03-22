package com.m1a2st.simplebackendpractice.user;

import com.m1a2st.simplebackendpractice.user.dto.UserProfileReqDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserProfileRespDTO;
import com.m1a2st.simplebackendpractice.user.enu.UserRole;
import com.m1a2st.simplebackendpractice.user.enu.UserStatus;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author m1a2st
 * @Date 2023/3/20
 * @Version v1.0
 */
@Slf4j
@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    @Value("user.not.found")
    private String UNKNOWN;

    public UserProfileService(UserProfileRepository userProfileRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProfileRespDTO signup(UserProfileReqDTO userProfileRespDTO) {
        String insertUsername = userProfileRespDTO.getUsername();
        if (userProfileRepository.existsByUsername(insertUsername)) {
            throw new IllegalArgumentException("This username is Duplicate");
        }
        UserProfile signupUser = UserProfile.builder()
                .username(userProfileRespDTO.getUsername())
                .password(passwordEncoder.encode(userProfileRespDTO.getPassword()))
                .role(UserRole.ROLE_DEFAULT)
                .status(UserStatus.ACTIVE)
                .build();
        userProfileRepository.save(signupUser);
        return new UserProfileRespDTO().toDto(signupUser, "Signup success");
    }

    public UserProfile login(String username, String password) {
        Optional<UserProfile> userProfile = userProfileRepository.findByUsername(username);
        if(userProfile.isPresent()){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return userProfile.get();
        }
        return UserProfile.builder().username(UNKNOWN).build();
    }
}
