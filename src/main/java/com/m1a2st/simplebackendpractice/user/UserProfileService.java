package com.m1a2st.simplebackendpractice.user;

import com.m1a2st.simplebackendpractice.user.dto.UserProfileReqDTO;
import com.m1a2st.simplebackendpractice.user.dto.UserProfileRespDTO;
import com.m1a2st.simplebackendpractice.user.enu.UserRole;
import com.m1a2st.simplebackendpractice.user.enu.UserStatus;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import lombok.extern.slf4j.Slf4j;
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

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfileRespDTO signUp(UserProfileReqDTO userProfileRespDTO) {
        UserProfile signUpUser = UserProfile.builder()
                .username(userProfileRespDTO.getUsername())
                .password(userProfileRespDTO.getPassword())
                .role(UserRole.DEFAULT)
                .status(UserStatus.ACTIVE)
                .build();
        userProfileRepository.save(signUpUser);
        return new UserProfileRespDTO().toDto(signUpUser,"Signup success");
    }
}
