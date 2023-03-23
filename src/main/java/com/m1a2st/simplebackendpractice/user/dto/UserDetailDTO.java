package com.m1a2st.simplebackendpractice.user.dto;

import com.m1a2st.simplebackendpractice.user.UserProfileService;
import com.m1a2st.simplebackendpractice.user.enu.UserStatus;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author m1a2st
 * @Date 2023/3/23
 * @Version v1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDetailDTO {

    private Long id;

    private String username;

    private UserStatus status;

    private Date createDate;

    public UserDetailDTO toDTO(UserProfile userProfile){
        return UserDetailDTO.builder()
                .id(userProfile.getId())
                .username(userProfile.getUsername())
                .status(userProfile.getStatus())
                .createDate(userProfile.getCreateDate())
                .build();
    }
}
