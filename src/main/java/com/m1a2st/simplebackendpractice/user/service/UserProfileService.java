package com.m1a2st.simplebackendpractice.user.service;

import com.m1a2st.simplebackendpractice.user.dto.*;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import org.springframework.data.domain.Page;

/**
 * @Author m1a2st
 * @Date 2023/3/24
 * @Version v1.0
 */
public interface UserProfileService {

    UserProfile queryByUsername(String username);

    UserSignupRespDTO signup(UserSignupReqDTO userProfileRespDTO);

    UserProfile login(String username, String password);

    UserDetailDTO getUserDetail(String username);

    void modifyPassword(String username, UserModifyPasswordDTO userModifyPasswordDTO);

    void stopAccount(String username);

    Page<UserLoginResponseDTO> queryUserRecord(String name, Integer page, Integer size);
}
