package com.m1a2st.simplebackendpractice.user.dto;

import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

/**
 * @Author m1a2st
 * @Date 2023/3/19
 * @Version v1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserProfileRespDTO {

    private Long id;
    private String username;
    private String message;

    public UserProfileRespDTO toDto(UserProfile userProfile, String message) {
        return UserProfileRespDTO.builder()
                .id(userProfile.getId())
                .username(userProfile.getUsername())
                .message(message)
                .build();
    }
}
