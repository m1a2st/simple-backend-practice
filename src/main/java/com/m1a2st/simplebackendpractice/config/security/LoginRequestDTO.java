package com.m1a2st.simplebackendpractice.config.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author m1a2st
 * @Date 2023/3/21
 * @Version v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    private String username;

    private String password;
}
