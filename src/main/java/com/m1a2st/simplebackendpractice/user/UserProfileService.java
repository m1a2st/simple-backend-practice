package com.m1a2st.simplebackendpractice.user;

import com.m1a2st.simplebackendpractice.user.dto.UserDetailDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserModifyPasswordDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserSignupReqDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserSignupRespDTO;
import com.m1a2st.simplebackendpractice.user.enu.UserRole;
import com.m1a2st.simplebackendpractice.user.enu.UserStatus;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public UserProfileService(UserProfileRepository userProfileRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProfile queryByUsername(String username) {
        return userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not Found"));
    }

    public UserSignupRespDTO signup(UserSignupReqDTO userProfileRespDTO) {
        String insertUsername = userProfileRespDTO.getUsername();
        if (userProfileRepository.existsByUsername(insertUsername)) {
            throw new IllegalArgumentException("This username is Duplicate");
        } else if (!userProfileRespDTO.isPasswordSame()) {
            throw new IllegalArgumentException("This password is not as same as repeatPassword.");
        }
        UserProfile signupUser = UserProfile.builder()
                .username(userProfileRespDTO.getUsername())
                .password(passwordEncoder.encode(userProfileRespDTO.getPassword()))
                .role(UserRole.ROLE_DEFAULT)
                .status(UserStatus.ACTIVE)
                .build();
        userProfileRepository.save(signupUser);
        return new UserSignupRespDTO().toDto(signupUser, "Signup success");
    }

    public UserProfile login(String username, String password) {
        UserProfile userProfile = queryByUsername(username);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return userProfile;
    }

    public UserDetailDTO getUserDetail(String username) {
        UserProfile userProfile = queryByUsername(username);
        return new UserDetailDTO().toDTO(userProfile);
    }

    public void modifyPassword(String username, UserModifyPasswordDTO userModifyPasswordDTO) {
        if (userModifyPasswordDTO.checkPassword()) {
            throw new IllegalArgumentException("Old password and new password are same.");
        }
        UserProfile userProfile = queryByUsername(username);
        if (passwordEncoder.matches(userModifyPasswordDTO.getOldPassword(), userProfile.getPassword())) {
            userProfile.setPassword(userModifyPasswordDTO.getNewPassword());
            userProfileRepository.save(userProfile);
        } else {
            throw new IllegalArgumentException("Password is wrong!");
        }

    }

    public void stopAccount(String username) {

    }
}
