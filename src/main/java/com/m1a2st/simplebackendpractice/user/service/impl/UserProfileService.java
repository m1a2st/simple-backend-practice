package com.m1a2st.simplebackendpractice.user.service.impl;

import com.m1a2st.simplebackendpractice.user.dto.*;
import com.m1a2st.simplebackendpractice.user.enu.UserLoginStatus;
import com.m1a2st.simplebackendpractice.user.enu.UserRole;
import com.m1a2st.simplebackendpractice.user.enu.UserStatus;
import com.m1a2st.simplebackendpractice.user.po.UserLoginDocument;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import com.m1a2st.simplebackendpractice.user.repository.UserLoginRepository;
import com.m1a2st.simplebackendpractice.user.repository.UserProfileRepository;
import com.m1a2st.simplebackendpractice.wallet.enu.WalletStatus;
import com.m1a2st.simplebackendpractice.wallet.po.Wallet;
import com.m1a2st.simplebackendpractice.wallet.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Author m1a2st
 * @Date 2023/3/20
 * @Version v1.0
 */
@Slf4j
@Service("userProfileService")
public class UserProfileService implements com.m1a2st.simplebackendpractice.user.service.UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserLoginRepository userLoginRepository;
    private final WalletRepository walletRepository;

    public UserProfileService(UserProfileRepository userProfileRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserLoginRepository userLoginRepository, WalletRepository walletRepository) {
        this.userProfileRepository = userProfileRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userLoginRepository = userLoginRepository;
        this.walletRepository = walletRepository;
    }

    @Cacheable(cacheNames = {"users"}, key="#username")
    @Override
    public UserProfile queryByUsername(String username) {
        return userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not Found"));
    }


    @Override
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
        walletRepository.save(Wallet.builder()
                .userId(signupUser.getId())
                .balance(BigDecimal.ZERO)
                .status(WalletStatus.ACTIVE)
                .build());
        userLoginRepository.save(UserLoginDocument.builder()
                .username(signupUser.getUsername())
                .userLoginStatus(UserLoginStatus.SIGNUP)
                .loginTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .build());
        return new UserSignupRespDTO().toDto(signupUser, "Signup success");
    }

    @CachePut(cacheNames = {"users"}, key="#result.username")
    @Override
    public UserProfile login(String username, String password) {
        UserProfile userProfile = queryByUsername(username);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        userLoginRepository.save(UserLoginDocument.builder()
                .username(username)
                .lastModifiedDate(userProfile.getLastModifiedDate())
                .userLoginStatus(UserLoginStatus.LOGIN)
                .loginTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .build());
        return userProfile;
    }

    @Override
    public UserDetailDTO getUserDetail(String username) {
        UserProfile userProfile = queryByUsername(username);
        return new UserDetailDTO().toDTO(userProfile);
    }

    @Override
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

    @CacheEvict(cacheNames = {"users"}, key = "#username")
    @Override
    public void stopAccount(String username) {
        UserProfile userProfile = queryByUsername(username);
        userProfile.setStatus(UserStatus.HIBERNATE);
        userProfileRepository.save(userProfile);
    }

    @Override
    public Page<UserLoginResponseDTO> queryUserRecord(String name, Integer page, Integer size) {
        Page<UserLoginDocument> loginPage = userLoginRepository
                .findAllByUsernameOrderByCreateByDesc(name, PageRequest.of(page, size));
        return loginPage.map(u -> UserLoginResponseDTO.builder()
                .createBy(u.getCreateBy())
                .createDate(u.getCreateDate())
                .lastModifiedDate(u.getLastModifiedDate())
                .loginTime(u.getLoginTime())
                .build());
    }
}
