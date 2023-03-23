package com.m1a2st.simplebackendpractice.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * @Author m1a2st
 * @Date 2023/3/19
 * @Version v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupReqDTO {

    @Schema(description = "使用者名稱")
    private String username;
    @Schema(description = "使用者密碼")
    private String password;
    @Schema(description = "密碼重複輸入驗證用")
    private String repeatPassword;

    public boolean isPasswordSame() {
        return Optional.ofNullable(password)
                .filter(p -> p.equals(repeatPassword))
                .isPresent();
    }
}
