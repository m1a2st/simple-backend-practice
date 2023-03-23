package com.m1a2st.simplebackendpractice.user.dto;

import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author m1a2st
 * @Date 2023/3/19
 * @Version v1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserSignupRespDTO {

    @Schema(description = "使用者編號")
    private Long id;
    @Schema(description = "使用者密碼")
    private String username;
    @Schema(description = "回傳訊息")
    private String message;

    public UserSignupRespDTO toDto(UserProfile userProfile, String message) {
        return UserSignupRespDTO.builder()
                .id(userProfile.getId())
                .username(userProfile.getUsername())
                .message(message)
                .build();
    }
}
