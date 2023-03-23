package com.m1a2st.simplebackendpractice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author m1a2st
 * @Date 2023/3/23
 * @Version v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyPasswordDTO {

    private String oldPassword;
    private String newPassword;

    public boolean checkPassword(){
        return oldPassword.equals(newPassword);
    }
}
