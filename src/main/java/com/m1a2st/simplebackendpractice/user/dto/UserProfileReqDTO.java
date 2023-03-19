package com.m1a2st.simplebackendpractice.user.dto;

import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author m1a2st
 * @Date 2023/3/19
 * @Version v1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileReqDTO {

    private String username;
    private String password;
    private String repeatPassword;

}
